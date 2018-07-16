package com.test;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class UserActivityReducer extends Reducer<CustomKey, Text, IntWritable, Text> {
	@Override
	protected void reduce(CustomKey key, Iterable<Text> values,
			Reducer<CustomKey, Text, IntWritable, Text>.Context context) throws IOException, InterruptedException {
		Iterator<Text> itr = values.iterator();
		Text userName = new Text(itr.next());
		while (itr.hasNext()) {
			Text activityInfo = itr.next();
			Text userActivityInfo = new Text(userName.toString() + "\t" + activityInfo.toString());
			context.write(new IntWritable(key.getUserId()), userActivityInfo);
		}
	}
}
