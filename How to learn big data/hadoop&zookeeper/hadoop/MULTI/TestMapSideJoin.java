package com.test;

import java.io.*;
import java.util.*;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import java.net.URI;
import java.util.HashMap;
import java.io.BufferedReader;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TestMapSideJoin {
	public static class Mapjoin extends Mapper<LongWritable, Text, Text, Text> {
		static BufferedReader br;
		HashMap<String, String> hmap = new HashMap<String, String>(); //분산 캐시로 분류가 되는 것 이걸로 조인 하게 된다. 

		public void setup(Context context) throws InterruptedException, IOException {
			URI cachefilelist[] = context.getCacheFiles();
			Path cachefile = new Path(cachefilelist[0]);
			br = new BufferedReader(new FileReader(cachefile.toString())); //여기서는 저장이 안될 것이다. HADOOP에 올려서 사용해야 한다. 
			String linestr;
			while ((linestr = br.readLine()) != null) {
				String[] l = linestr.split(",");
				hmap.put(l[0].trim(), l[1].trim());// 여기에 있는 primary key와 연결되어 사용하게 된다. 
			}
		}

		public void map(LongWritable k, Text v, Context context) throws InterruptedException, IOException {
			String line = v.toString();
			String[] emp = line.split(",");// 10,smith,clerk,4000
			String deptno = emp[3];//
			String dname;
			dname = hmap.get(deptno);
			context.write(v, new Text(dname)); //dept하여 레퍼와 조인하게 된다. 
		}

	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "job1");
		URI cachefile = new URI(args[0]); // Dept
		job.addCacheFile(cachefile);
		// job.addCacheFile(new
		// Path("hdfs://localhost:8020/user/hadoop/Employee.txt").toUri());//alternative
		// to above two lines
		// cache 영역을 만들어 사용하게 된다. 
		// 전체적으로 분산 캐시를 이용하여 만들게 된다. 
		FileInputFormat.addInputPath(job, new Path(args[1])); // Employee
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setMapperClass(Mapjoin.class);
		job.setJarByClass(TestMapSideJoin.class);
		job.waitForCompletion(true);
	}
}
