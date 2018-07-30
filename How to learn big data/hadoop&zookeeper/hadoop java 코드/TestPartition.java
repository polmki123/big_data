package com.test;

import java.io.*;
import java.util.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TestPartition {
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
		public void map(LongWritable k, Text v, Context context) throws InterruptedException, IOException {
			String string = v.toString();
			String[] str = string.split(",");
			int sal = Integer.parseInt(str[2]);
			String deptno = str[3];
			context.write(new Text(deptno), new IntWritable(sal));
		}
	}

	public static class Partition extends Partitioner<Text, IntWritable> {
		public int getPartition(Text k, IntWritable v, int p) {
			int sal = v.get();
			if (sal < 1500) {
				return 0;
			} else
				return 1;
		}
	}

	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text k, Iterable<IntWritable> v, Context context) throws InterruptedException, IOException {
			Iterator<IntWritable> it = v.iterator();
			int sumofsal = 0;
			while (it.hasNext()) {
				sumofsal = sumofsal + it.next().get();
			}
			context.write(k, new IntWritable(sumofsal));
		}

	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "job1");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setMapperClass(Map.class);
		job.setCombinerClass(Reduce.class);
		job.setNumReduceTasks(2);
		job.setPartitionerClass(Partition.class);
		job.setReducerClass(Reduce.class);
		job.setJarByClass(TestPartition.class);
		job.waitForCompletion(true);
	}
}