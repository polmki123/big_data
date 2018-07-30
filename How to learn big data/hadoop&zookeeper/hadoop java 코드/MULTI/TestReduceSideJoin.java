package com.test;

import java.io.*;
import java.util.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TestReduceSideJoin {
	public static class MapEmp extends Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String emp[] = line.split(",");
			String prefix = "e.".concat(line);
			String deptno = emp[3];
			context.write(new Text(deptno), new Text(prefix));
		}
	}

	public static class MapDept extends Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String dept[] = line.split(",");
			String prefix = "d.".concat(line);
			String deptno = dept[0];
			context.write(new Text(deptno), new Text(prefix));
		}
	}

	public static class Reduce11 extends Reducer<Text, Text, Text, Text> {
		public void reduce(Text k, Iterable<Text> v, Context context) throws IOException, InterruptedException {
			Iterator<Text> it = v.iterator();
			List<String> eList = new ArrayList();
			List<String> dList = new ArrayList();
			eList.clear();
			dList.clear();
			String line;
			while (it.hasNext()) {
				line = it.next().toString();
				if (line.substring(0, 1).equals("e")) {
					eList.add(line);
				} else {
					dList.add(line);
				}
			}
			// List creation completed
			// start loop emp and nested loop dept
			for (String e : eList) {
				for (String d : dList) {
					context.write(new Text(e), new Text(d));
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "job1");
		job.setOutputFormatClass(TextOutputFormat.class);
		MultipleInputs.addInputPath(job, new Path(args[0]), // Employee.txt
				TextInputFormat.class, MapEmp.class);
		MultipleInputs.addInputPath(job, new Path(args[1]), // Dept.txt
				TextInputFormat.class, MapDept.class);
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setJarByClass(TestReduceSideJoin.class);
		job.setReducerClass(Reduce11.class);
		job.setMapperClass(MapEmp.class);
		job.setMapperClass(MapDept.class);
		job.waitForCompletion(true);
		System.out.println("Main End");
	}
}