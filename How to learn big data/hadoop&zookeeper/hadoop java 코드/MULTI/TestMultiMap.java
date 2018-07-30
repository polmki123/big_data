package com.test;

import java.io.*;
import java.util.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TestMultiMap {
	public static class MapEmp extends Mapper<LongWritable, Text, IntWritable, Text> {
		public void map(LongWritable k, Text v, Context context) throws InterruptedException, IOException {
			String stre = v.toString();
			String[] line = stre.split(",");
			int deptno = Integer.parseInt(line[4]);
			context.write(new IntWritable(deptno), v);
		}
	}

	public static class MapDept extends Mapper<LongWritable, Text, IntWritable, Text> {
		public void map(LongWritable k, Text v, Context context) throws InterruptedException, IOException {
			String strd = v.toString();
			String[] line1 = strd.split(",");
			int deptnum = Integer.parseInt(line1[0]);
			context.write(new IntWritable(deptnum), v);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "job1");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, MapEmp.class);
		MultipleInputs.addInputPath(job, new Path(args[2]), TextInputFormat.class, MapDept.class);
		FileOutputFormat.setOutputPath(job, new Path(args[3]));
		뇬
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);
		job.setMapperClass(MapEmp.class);
		job.setMapperClass(MapDept.class);
		job.setJarByClass(TestMultiMap.class);
		job.waitForCompletion(true);
	}
}
