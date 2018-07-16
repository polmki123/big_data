package com.test;
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.javamakeuse.bd.poc.util.CustomKey;

public class UserActivityMapper extends Mapper<LongWritable, Text, CustomKey, Text> {

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, CustomKey, Text>.Context context)
			throws IOException, InterruptedException {

		String[] columns = value.toString().split("\\|");

		if (columns != null && columns.length > 3) {
			// dataSetType 2=user_activity.log data
			context.write(new CustomKey(Integer.parseInt(columns[1]), 2), new Text(columns[2] + "\t" + columns[3]));
		}
	}
}
