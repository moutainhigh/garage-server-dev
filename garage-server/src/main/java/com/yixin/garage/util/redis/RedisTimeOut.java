package com.yixin.garage.util.redis;

public interface RedisTimeOut {
	int HOUR = 3600;
	int DAY = 3600*24;
    int THREE_DAY = DAY*3;
	int WEEK = DAY*7;
	int MONTH = DAY*30;
	int THREE_MONTH = DAY*30*3;
	int QUARTER = 15*60; //一刻钟
	
}
