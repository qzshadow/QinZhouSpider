# -*- coding: utf-8 -*-
"""
Created on Fri May 15 22:27:59 2015

@author: Qin
"""

import os
import pandas as pd
from matplotlib import pyplot as plt
from datetime import datetime
import socket
import numpy as np
if os.path.exists(r"C:\Users\Qin\IdeaProjects\QinZhouSpider\log\log"):
    logFile = pd.read_csv(r"C:\Users\Qin\IdeaProjects\QinZhouSpider\log\log",error_bad_lines=False,encoding='utf-8',parse_dates=True, names=['DateTime','Thread_Num','Status','Tag','Level','Url'])
logFile = logFile.dropna(how='any')
if len(logFile) == 0:
    print('there are no contents in log file, exiting...')
else:
    log_len = len(logFile)
    time_consuming = convert_timestamp_to_index(logFile['DateTime'][0], logFile['DateTime'][log_len-1])
    page_num = int(log_len/3)
    max_level = int(max(logFile['Level']))
    print('total time consume(sec):', time_consuming)
    print('Web page max depth(from 0):', max_level)
    print('page numbers:', page_num)
    print('Web crawling speed(page/sec):',page_num/time_consuming)
    
    page_list = []
    for i in range(max_level+1):
        page_list.append(logFile[logFile['Level'] == i]['Url'].values)
    
    page_stat = np.zeros((max_level+1,2))
    for i in range(max_level+1):
        for url in page_list[i]:
            if in_site(url):
                page_stat[i][0] +=1
            else:
                page_stat[i][1] += 1
                
    x = np.arange(max_level+1)
    width = 0.1
    plt.bar(x,page_stat[:,0],width,color='b',label='insite')
    plt.bar(x+width,page_stat[:,1],width,color='r',label = 'outsite')
    plt.xlabel('level')
    plt.ylabel('page numbers')
    plt.legend(loc='upper left')
    plt.title('relationship between depth and pages')
    plt.xticks(x+width,x)

def in_site(url):
    return '100step' in url
    
def get_ip(url):
    ip_addr = socket.getaddrinfo(url,'http')[0][4][0]
    return ip_addr

def convert_timestamp_to_index(min_timestamp, timestamp):
    min_datetime = datetime(int(min_timestamp[:4]),int(min_timestamp[5:7]),int(min_timestamp[8:10]),int(min_timestamp[11:13]),int(min_timestamp[14:16]),int(min_timestamp[17:19]),int(min_timestamp[20:23]))
    now_datetime = datetime(int(timestamp[:4]),int(timestamp[5:7]),int(timestamp[8:10]),int(timestamp[11:13]),int(timestamp[14:16]),int(timestamp[17:19]),int(timestamp[20:23]))
    return (now_datetime - min_datetime).seconds
