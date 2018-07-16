package com.test;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class UserMapper extends Mapper<LongWritable, Text, CustomKey, Text> {
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, CustomKey, Text>.Context context)
			throws IOException, InterruptedException {
		String[] columns = value.toString().split("\t");
		int userId = Integer.parseInt(columns[0]);
		// dataSetType 1= user.log data
		context.write(new CustomKey(userId, 1), new Text(columns[1]));
	}
}
