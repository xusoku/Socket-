package com.cooge.media.proxy.model;

import com.cooge.media.proxy.model.DownMsg;

interface IMsgService{
 	DownMsg getPrice(String key);
}