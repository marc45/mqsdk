# startai-mqsdk 3.0集成指南
### 概要
本文描述如何使用 startai-mqsdk 3.0，快速将Android手机或Android智能硬件接入StartAI物联网云平台（以下简称“StartAI云平台”）。  
####1.SDK目的与功能
StartAI云平台接入端(Android)SDK（以下简称“SDK”）封装了手机（包括PAD等设备）与StartAI云平台智能硬件的通讯过程，以及手机与云端的通讯过程。这些过程包括用户注册、登录、配置设备入网、发现、绑定、控制等。使用SDK，可以使得开发者快速完成APP开发，开发者仅需关注APP的UI设计即可，而相对复杂的协议与错误处理等事项可忽略。

####2.云平台方案概况

![](https://i.imgur.com/fAmGXLP.jpg)
####3.集成准备
#####3.1.注册StartAI云平台账号
在使用StartAI云平台服务前，你需要通过dev.startai.com.cn注册一个开发者账号。请完整填写你的注册信息。
#####3.2.创建app并获取到 appID
#####3.3.下载SDK demo程序 
[demo下载](https://github.com/luobinxin/mqsdk)
 



### SDK流程简介
####1.通用流程图
![](https://i.imgur.com/k9ORX4J.png)
####2.关键点说明

1）SDK已经封装了所有的用户注册、登录、配置、发现、绑定、控制的过程，开发者使用这些API可以完成上述流程中的功能开发，不需要再自行实现通讯协议。

2）SDK采取回调的工作方式，所以必须设置必要的监听，具体请参见流程详解。

### SDK流程详解
####1.初始化部分
#####1.1.初始化部分流程图
![](https://i.imgur.com/BPQirYp.png)
#####1.2.环境搭建
######1.2.1.引入库，在gradle中添加

    //mqtt 基础包
    implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.0'
    //startai mqtt  sdk包
    implementation 'cn.com.startai:mqsdk:3.1.12'
    //json解析
    implementation 'com.google.code.gson:gson:2.8.5'
    //startai airkiss sdk
    implementation 'cn.com.startai:airkisssdk:1.0.3'
    //startai esptouch sdk
    compile 'cn.com.startai:esptouchsdk:1.0.3'
    //startai udp sdk
    implementation 'cn.com.startai:udpsdk:1.0.2'
 
######1.2.2.权限      
       
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
	    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
	    <uses-permission android:name="android.permission.INTERNET" />
    	<uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE" />

#####1.3. 注册监听器
######1.3.1.连接事件监听器
       
	 PersistentEventDispatcher.getInstance().registerICommonStateListener(new ICommonStateListener() {
            @Override
            public void onConnectFail(int errorCode, String errorMsg) {
				//连接失败
            }

            @Override
            public void onConnected() {
				//连接成功
            }

            @Override
            public void onDisconnect(int errorCode, String errorMsg) {
				//连接断开，sdk会自动重连
            }

            /**
             * 登录 tokent 失效
             *
             * @param resp
             */
            @Override
            public void onTokenExpire(C_0x8018.Resp.ContentBean resp) {
                TAndL.TL(getApplicationContext(), "token过期，" + resp);
  				//token开发者需要在此处重新登录
            }
        });

######1.3.2.消息发送结果监听器

在发送消息的时候携带

	    /**
	     * 消息发送的结果回调
	     */
	    IOnCallListener onCallListener = new IOnCallListener() {
	        /**
	         * 消息发送成功
	         * @param request
	         */
	        @Override
	        public void onSuccess(MqttPublishRequest request) {
	        }
	
	        /**
	         * 消息发送失败
	         * @param request
	         * @param startaiError
	         */
	        @Override
	        public void onFailed(MqttPublishRequest request, StartaiError startaiError) {
	 			//消息发送失败后可调用通用消息发送方法重发
            	StartAI.getInstance().send(request, this);
	        }
	
	        /**
	         * 是否回调到ui线程
	         * @return
	         */
	        @Override
	        public boolean needUISafety() {
	            return false;
	        }

######1.3.2.业务处理结果回调



	 PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 	 
	 //注销消息回调
	 PersistentEventDispatcher.getInstance().unregisterOnPushListener(listener);

	 AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

        /**
		 * 所有的回调是否到ui线程
		 * @return
         */
        @Override
        public boolean needUISafety() {
            return true;
        }

        /**
         * 通用的消息接收方法,除基础业务以外的消息都回回调到此方法
         * @param topic   接收消息的主题
         * @param msgtype 消息类型
         * @param result  1成功 0失败
         * @param msg     消息内容
         */
        @Override
        public void onCommand(String topic, String msg) {

        }

        /**
         * 设备激活回调，如果激活成功只会回调一次
         * @param initResult 激活成功 1|失败 0
         * @param errcode    失败的异常码 ，成功为""
         * @param errmsg     失败的异常码描述 ， 成功为""
         */
        @Override
        public void onActiviteResult(int initResult, String errcode, String errmsg) {

            //在sdk首初始化时会回调，开发者发送消息必需在此接口回调成功之后才可以进行

        }

    };

    

#####1.4.初始化sdk	


        String appid = "1W5D8WER45WQER1R5WEW666EWRF5BBE";//开发者平台获取

        MqttInitParam initParam = new MqttInitParam(appid);

        StartAI.getInstance().initialization(getApplicationContext(), initParam);

####2.用户部分
StartAI云平台的用户系统包含了用户的注册、登录、重置密码、修改个人信息等功能，StartAI云平台以DOMAIN区分用户系统，不同DOMAIN的用户系统相互独立。更换DOMAIN后，需要重新注册用户。
#####2.1.用户部分流程图
![](https://i.imgur.com/OrcXqMU.png)
#####2.2.注册部分
StartAI云平台提供两种用户注册方式：手机注册、邮箱注册。
######2.2.1.手机注册
通过手机注册账号，需要一个有效的手机号。注册时需要三步操作：获取短信验证码、校验验证码、注册。

第一步：获取验证码，APP获取短信验证码时，SDK向云端发送短信验证码请求，如果请求成功，云端会给手机发送短信验证码。

【示例代码】
	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	StartAI.getInstance().getBaseBusiManager().getIdentifyCode("13333333333", Type.GetIdentifyCode.REGISTER, onCallListener);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onGetIdentifyCodeResult(C_0x8021.Resp resp) {
 
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            //获取验证码成功
	        } else {
	            //获取验证码失败
	        }
	    }


	}


第二步：校验验证码，用短信验证码注册时，APP把手机收到的短信验证码传给SDK，进行校验。

【示例代码】

	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	StartAI.getInstance().getBaseBusiManager().checkIdentifyCode("13333333333", identify, Type.CheckIdentifyCode.REGISTER, onCallListener);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {
	
	    @Override
	    public void onCheckIdetifyResult(C_0x8022.Resp resp) {
 
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            //校验验证码成功
	        } else {
	            //校验验证码失败
	        }
	    }


	}


第三步：注册

【示例代码】

	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	StartAI.getInstance().getBaseBusiManager().register("13333333333", "123456", onCallListener);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {
	
	    @Override
	    public void onRegisterResult(C_0x8017.Resp resp) {
	        TAndL.TL(getApplicationContext(), "注册结果 result = " + resp);
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            //注册成功
	        } else {
	            //注册失败
	        }
	    }


	}


######2.2.2.邮箱注册
第一步：注册，注册成功后，StartAI云平台会发送激活邮件到指定邮箱

【示例代码】

	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	StartAI.getInstance().getBaseBusiManager().register("111111111@qq.com", "123456", onCallListener);
	
	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {
	
	    @Override
	    public void onRegisterResult(C_0x8017.Resp resp) {
	        TAndL.TL(getApplicationContext(), "注册结果 result = " + resp);
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            //注册成功
	        } else {
	            //注册失败
	        }
	    }


	}
 

第二步：打开邮件按照邮箱指定点击激活链接对注册帐号进行激活，激活过后的邮箱账号才能正常登录。

【示范图片】

![](https://i.imgur.com/or5kdGC.png)

#####2.3.登录部分
用户登录时，用户名可以是注册过的手机号、邮箱。登录账号要先注册好，也可以直接使用手机进行快捷登录，如果更换了Domain，登录账号需要重新注册。
######2.3.1快捷登录

用户可通过手机获取验证码直接快捷登录。快捷登录需要两步

第一步：获取验证码

【示例代码】

	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	StartAI.getInstance().getBaseBusiManager().getIdentifyCode("11111111@qq.com", Type.GetIdentifyCode.LOGIN, onCallListener);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onGetIdentifyCodeResult(C_0x8021.Resp resp) {
 
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            //获取验证码成功
	        } else {
	            //获取验证码失败
	        }
	    }


	}


第二步：登录
            
【示例代码】	
	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	StartAI.getInstance().getBaseBusiManager().login("13333333333", "", "009527", onCallListener);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onLoginResult( C_0x8018.Resp resp) {
	
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            TAndL.TL(getApplicationContext(), "登录成功 " + resp);
	            //开发者需要在此保存登录信息
	        } else {
	            TAndL.TL(getApplicationContext(), "登录失败 " + resp);
	        }
	
	    }
 
	}




######2.3.2手机号加密码登录

【示例代码】

  
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	StartAI.getInstance().getBaseBusiManager().login("13333333333", "123456", "", onCallListener);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onLoginResult( C_0x8018.Resp resp) {
	
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            TAndL.TL(getApplicationContext(), "登录成功 " + resp);
				//开发者需要在此保存登录信息
	        } else  {
	            TAndL.TL(getApplicationContext(), "登录失败 " + resp);
	        }
	
	    }
 
	}

######2.3.3邮箱号加密码登录

【示例代码】

 
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	StartAI.getInstance().getBaseBusiManager().login("11111111@qq.com", "123456", "", onCallListener);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onLoginResult( C_0x8018.Resp resp) {
	
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            TAndL.TL(getApplicationContext(), "登录成功 " + resp);
				//开发者需要在此保存登录信息
	        } else  {
	            TAndL.TL(getApplicationContext(), "登录失败 " + resp);
	        }
	
	    }
 
	}
 

######2.3.4用户名加密码登录

【示例代码】

 
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	StartAI.getInstance().getBaseBusiManager().login("jobs", "123456", "", onCallListener);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onLoginResult( C_0x8018.Resp resp) {
	
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            TAndL.TL(getApplicationContext(), "登录成功 " + resp);
				//开发者需要在此保存登录信息
	        } else  {
	            TAndL.TL(getApplicationContext(), "登录失败 " + resp);
	        }
	
	    }
 
	}


######2.3.5双重验证登录

当用户长时间未登录时，需要用户进行双重验证登录，需要同时输入手机号加密码加验证码进行双重验证登录,登录之前需要获取动态验证码


 
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	StartAI.getInstance().getBaseBusiManager().login("jobs", "123456", "009527", onCallListener);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onLoginResult( C_0x8018.Resp resp) {
	
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            TAndL.TL(getApplicationContext(), "登录成功 " + resp);
				//开发者需要在此保存登录信息
	        } else  {
	            TAndL.TL(getApplicationContext(), "登录失败 " + resp);
	        }
	
	    }
 
	}



【示例代码】

######2.3.5第三方登录
目前支持的第三方账号有微信。用户可以使用第三方的API获取到code登录StartAI云，使用第三方账号登录时无需在StartAI云上注册，可直接登录。

开发者可通过微信api获取code, 具体方法请参考各第三方平台的开发者文档。

【示例代码】

 
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	StartAI.getInstance().getBaseBusiManager().loginWithThirdAccount(Type.Login.THIRD_WECHAT,"WechatCode",onCallListener);


	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onLoginResult( C_0x8018.Resp resp) {
	
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            TAndL.TL(getApplicationContext(), "登录成功 " + resp);
				//开发者需要在此保存登录信息
	        } else  {
	            TAndL.TL(getApplicationContext(), "登录失败 " + resp);
	        }
	
	    }
 
	}


#####2.4.修改密码部分

用户登录后可以修改密码。
【示例代码】

 
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求 ，如果是通过验证码快捷登录的用户设置登录密码则 oldpwd填 "" 空字符串
	StartAI.getInstance().getBaseBusiManager().updateUserPwd("oldPwd", "newPwd", onCallListener);


	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onLoginResult( C_0x8018.Resp resp) {
	
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            TAndL.TL(getApplicationContext(), "登录成功 " + resp);
				//开发者需要在此保存登录信息
	        } else  {
	            TAndL.TL(getApplicationContext(), "登录失败 " + resp);
	        }
	
	    }
 
	}





#####2.5.重置密码部分

如果忘记了用户密码，可以通过手机验证码或邮箱设置新的密码。SDK支持手机号重置密码和邮箱重置密码两种，手机号重置需要接收验证码，邮箱重置需要进入邮箱，根据链接提示进行重置。
#####2.5.1.手机号重置密码时，需要先获取短信验证码再重置。获取短信验证码方式与手机注册时相同。
第一步：获取短信验证码

 

【示例代码】
 
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求 
	StartAI.getInstance().getBaseBusiManager().getIdentifyCode("13333333333", Type.GetIdentifyCode.FORGET_PWD, onCallListener);


	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {


	    @Override
	    public void onGetIdentifyCodeResult(C_0x8021.Resp resp) {
	        super.onGetIdentifyCodeResult(resp);
	         
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            //获取验证码成功
	        } else {
	            //获取验证失败
	        }
	    }

 
	}



第二步：检验验证码

【示例代码】

	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	StartAI.getInstance().getBaseBusiManager().checkIdentifyCode(mobile, code, Type.CheckIdentifyCode.FORGET_PWD, onCallListener);


	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {


	    @Override
	    public void onCheckIdetifyResult(C_0x8022.Resp resp) {
	        TAndL.TL(getApplicationContext(), "校验验证码结果 result = " + resp);
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            //校验验证码成功
	        } else {
	            //校验验证码失败
	        }
	    }
	 
	}



第三步：重置密码



【示例代码】


	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求 
	StartAI.getInstance().getBaseBusiManager().resetMobileLoginPwd(mobile, pwd, onCallListener);


	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onResetMobileLoginPwdResult(C_0x8026.Resp resp) {
	        super.onResetMobileLoginPwdResult(resp);
	
	        if (resp.getResult() == 1) {
	            TAndL.TL(getApplicationContext(), "重置密码成功 " + resp);
	
	        } else {
	            TAndL.TL(getApplicationContext(), "重置密码失败 " + resp);
	        }
	
	    }
 
	}
 

#####2.5.2.邮箱号重置密码时，云端会给指定邮箱发送安全链接。用户需要到邮箱中查收邮件，并按邮件指示执行重置操作。重置密码邮件有可能进入用户的邮箱的垃圾箱中，需提醒用户。

【示例代码】
	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求 
	StartAI.getInstance().getBaseBusiManager().sendEmail(email, Type.SendEmail.FORGET_PWD, onCallListener);


	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onSendEmailResult(  C_0x8023.Resp resp) {
	
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            TAndL.TL(getApplicationContext(), "重置密码邮件发送成功 " + resp);
	        } else {
	            TAndL.TL(getApplicationContext(), "重置密码邮件发送失败 " + resp);
	        }
	
	    }
 
	}


#####2.6.查询用户信息

【示例代码】
	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求 
	StartAI.getInstance().getBaseBusiManager().getUserInfo(currUser.getUserid(), onCallListener);


	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onGetUserInfoResult(C_0x8024.Resp resp) {
	        super.onGetUserInfoResult(resp);
	        if (resp.getResult() ==  resp.RESULT_SUCCESS) {
	            //查询用户信息成功
	        }else{
				//查询用户信息失败
			}
	    
	    }
 
	}

#####2.7.修改用户信息

【示例代码】

	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求 
	C_0x8020.Req.ContentBean contentBean = new C_0x8020.Req.ContentBean();
	contentBean.setNickName(nickName);
	StartAI.getInstance().getBaseBusiManager().updateUserInfo(contentBean, onCallListener);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {

	    @Override
	    public void onUpdateUserInfoResult(C_0x8020.Resp resp) {
	        if (resp.getResult() == resp.RESULT_SUCCESS) {
	            TAndL.TL(getApplicationContext(), "用户信息修改成功 " + resp);

	        } else {
	            TAndL.TL(getApplicationContext(), "昵称修改失败 " + resp);
	        }
	
	    }
 
	}



####3.配置设备入网部分
控制设备前，需要先让设备连到路由器上。连上路由器的设备，如果路由器能接入外网，设备会自动注册到StartAI云平台。
Airkiss，Esptouch都是使用UDP广播方式，由手机端发出含有目标路由器名称和密码的广播，设备上的Wifi模块接收到广播包后自动连接目标路由器，连上路由器后发出配置成功广播，通知手机配置已完成。两种配网方式二选一即可。
#####3.1.配置设备入网部分流程图
![](https://i.imgur.com/If3xfav.png)
#####3.2.Airkiss
【示例代码】
		
		//设置监听
        StartaiAirkissManager.getInstance().setAirKissListener(new C2JavaExDevice.OnAirKissListener() {
            @Override
            public void onAirKissSuccess() {
                appendLog("配置成功 用时 " + ((System.currentTimeMillis() - t) / 1000) + " s\n");
                AirkissHelper.getInstance().stop();

            }

            @Override
            public void onAirKissFailed(int error) {
                appendLog("配置失败 errorCode = " + error + "\n");
                Java2CExDevice.stopAirKiss();
                AirkissHelper.getInstance().stop();
            }
        });


		String pwd = "1234567890";//wifi密码
		String ssid = "MI_WIFI";//wifi 名称
		String aes = "";//不填
		long timeout = 1000 * 90;//超时时间 ms
		int processPeroid = 0;//配网发包流程间隔 0-5000 ms
        int datePeroid = 5; //配网每包数据之间间隔 5-80 ms
		//开始配置 
		StartaiAirkissManager.getInstance().startAirKiss(pwd, ssid, aesKey.getBytes(), timeout, processPeroid, datePeroid);

		//停止配置
		Java2CExDevice.stopAirKiss();

		//下面为配网辅助代码，可按需要添加
		//开始辅助配网
		AirkissHelper.getInstance().start(timeout, new AirkissHelper.AirkissHelperListener() {
			@Override
			public void onAirkissSuccess(InetAddress inetAddress) {
				appendLog("配置成功 用时 " + ((System.currentTimeMillis() - t) / 1000) + " s\n");
			}
		});
		
		//停止辅助配网
		AirkissHelper.getInstance().stop();

#####3.3.Esptouch
【示例代码】

	    /**
	     * 设置回调
	     */
	    private MyEsptouchListener myListener = new MyEsptouchListener() {
	
	        @Override
	        public void onEspTouchResultFailed(final String errorCode, final String errorMsg) {
	            appendLog("配置失败 " + errorMsg + " errorCode = " + errorCode);
	            stopEsptouch();
	        }
	
	        @Override
	        public void onEsptouchResultAdded(final IEsptouchResult result) {
	            appendLog("配置成功 用时 " + ((System.currentTimeMillis() - t) / 1000) + " s " + " " + result.getBssid() + "\n");
	            stopEsptouch();
	
	        }
	    };

		开始配置
		EsptouchAsyncTask mTask = new EsptouchAsyncTask(getApplicationContext(), bssid, ssid, pwd, deviceCount, timeout, myListener);
		mTask.execute();

		//停止配置
        if (mTask != null) {
            mTask.cancelEsptouch();
            mTask = null;
        }		

		//下面为配网辅助代码，可按需要添加，如果开户了，需要在停止配网的同时，停止配网辅助
		//开始辅助配网
		AirkissHelper.getInstance().start(timeout, new AirkissHelper.AirkissHelperListener() {
			@Override
			public void onAirkissSuccess(InetAddress inetAddress) {
				appendLog("配置成功 用时 " + ((System.currentTimeMillis() - t) / 1000) + " s\n");
			}
		});
		
		//停止辅助配网
        AirkissHelper.getInstance().stop();


####4.设备发现与绑定
#####4.1.设备发现与绑定流程图
![](https://i.imgur.com/oZLmoz5.png)
#####4.1.2.局域网设备发现

如果设备与app处于同一wifi下，可通过局域网发现，查找设备。

【示例代码】

	 
#####4.1.2.扫码添加

对于不在同一局域网的设备，可以通过扫码的方式获取得设备的sn再进行设备绑定，二维码扫描相关代码需要开发者自行编写。
#####4.2.设备绑定
#####4.2.1.局域网绑定
【示例代码】



#####4.2.2.广域网绑定
【示例代码】

	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求 
	StartAI.getInstance().getBaseBusiManager().bind("device sn", onCallListener);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {


	    @Override
	    public void onBindResult(C_0x8002.Resp errResp, String id, C_0x8002.Resp.ContentBean.BebindingBean bebinding) {

	        TAndL.TL(getApplicationContext(), "添加结果  " + errResp);
			//如果绑定成功 开发者需要保存对端的相关信息
	    }

 
	}


#####4.3.设备解绑
#####4.3.1.局域网解绑
【示例代码】



#####4.3.2.广域网解绑
【示例代码】

	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求 
	StartAI.getInstance().getBaseBusiManager().unBind("device sn", onCallListener);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {


	    @Override
	    public void onUnBindResult(C_0x8004.Resp resp, String id, String beUnbindid) {
	        super.onUnBindResult(resp, id, beUnbindid);
	
	
	        TAndL.TL(getApplicationContext(), "删除结果 " + resp);
	
	        if (resp.getResult() == 1) {
	            StartAI.getInstance().getBaseBusiManager().getBindList(1, onCallListener);
	        }
	
	    }
 
	}




####5.设备控制
#####5.1.设备控制流程图
![](https://i.imgur.com/99wTJdz.png)
#####5.2.消息透传
######5.2.1局域网通信
【示例代码】

######5.2.2广域网透传

【示例代码】
	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	//发送字符串 
	String hexStr = "FFEE01200011220000"
	StartAI.getInstance().getBaseBusiManager().passthrough("deviceSN", hexStr, onCallListener);

	//发送字节数组
	//byte[] bytes =new byte[]{00,11,22,33,44};
	//StartAI.getInstance().getBaseBusiManager().passthrough(device.getId(), bytes, lis);

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {


	    @Override
	    public void onPassthroughResult(C_0x8200.Resp resp, String dataString, byte[] dataByteArray) {
	        super.onPassthroughResult(resp, dataString, dataByteArray);
	        
			TAndL.TL(getApplicationContext(),"接收到来自 "+resp.getFromid()+" 的消息 msg = "+dataString);			

	    }

 
	}



>请求成功及失败数据包格式(以查询绑定列表为例)
    
    //正常       
    {
	    "fromid": "Cloud/BXTM",
	    "m_ver": "Json_1.2.4_9.2.1",
	    "msgcw": "0x08",
	    "msgtype": "0x8005",
	    "result": 1,
	    "toid": "8e55c1e1ddc6c2249fc4daa2319a0f6d",
	    "ts": 1530499287362,
	    "content": [
	        {
	            "apptype": "smartBox",
	            "bindingtime": 1530257761935,
	            "connstatus": 1,
	            "id": "736F6C863300F4EB1EE79CC8015B503D",
	            "topic": "Q/client/736F6C863300F4EB1EE79CC8015B503D",
	            "type": 1
	        },
	        {
	            "apptype": "smartCtrl",
	            "bindingtime": 1530087011556,
	            "connstatus": 1,
	            "id": "7D7E77A1578AE531BB78C8170B18F47C",
	            "topic": "Q/client/7D7E77A1578AE531BB78C8170B18F47C",
	            "type": 6
	        }
	    ]
    }
    //失败   
    {
	    "fromid": "Cloud/BXTM",
	    "m_ver": "Json_1.2.4_9.2.1",
	    "msgcw": "0x08",
	    "msgtype": "0x8005",
	    "result": 0,
	    "toid": "8e55c1e1ddc6c2249fc4daa2319a0f6d",
	    "ts": 1530499287362,
	    "content": {
	        "errcode": "0x800203",
	        "errmsg": "被绑定id无效"
	    }
    }  


####6.其他接口


######绑定第三方账号

【示例代码】
	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求 接口调用前需要调用 微信的第三方登录SDK 授权api 拿到 code 
	C_0x8037.Req.ContentBean req = new C_0x8037.Req.ContentBean();
	req.setCode(resp.code); //code 来自微信授权返回
	req.setType(C_0x8037.THIRD_WECHAT); //绑定微信账号
	StartAI.getInstance().getBaseBusiManager().bindThirdAccount(req,onCallListener)

	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {


	    @Override
	    public void onBindThirdAccountResult(C_0x8037.Resp resp) { 
	        
			if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
				//成功

			}else{
				//失败
                            
			} 	

	    }

 
	}


######解绑第三方账号
【示例代码】
	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求
	C_0x8036.Req.ContentBean req = new C_0x8036.Req.ContentBean("userId", C_0x8036.THIRD_WECHAT);  //解绑 微信
	StartAI.getInstance().getBaseBusiManager().unBindThirdAccount(req,onCallListener)


	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {


	    @Override
	    public void onUnBindThirdAccountResult(C_0x8036.Resp resp) { 
	        
			if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
				//成功

			}else{
				//失败
                            
			} 	

	    }

 
	}
######更改/添加手机号

【示例代码】
	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求 接口调用前需要先 调用 获取验证码，检验验证码
	C_0x8034.Req.ContentBean req = new C_0x8034.Req.ContentBean("userId", mobile); //mobile 需要绑定的手机号
	StartAI.getInstance().getBaseBusiManager().bindMobileNum(req,onCallListener)


	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {


	    @Override
	    public void onBindMobileNumResult(C_0x8034.Resp resp) { 
	        
			if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
				//成功

			}else{
				//失败
                            
			} 	

	    }

 
	}
######获取APP支付宝 登录/授权 加密信息
【示例代码】
	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求  
	StartAI.getInstance().getBaseBusiManager().getAlipayAuthInfo(C_0x8033.AUTH_TYPE_LOGIN,onCallListener)


	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {


	    @Override
	    public void onGetAlipayAuthInfoResult(C_0x8033.Resp resp) { 
	        
			if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
				//成功

			}else{
				//失败
                            
			} 	

	    }

 
	}
######查询支付支付结果
【示例代码】
	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求  
	StartAI.getInstance().getBaseBusiManager().getRealOrderPayStatus("oerderNum",onCallListener)


	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {


	    @Override
	    public void onGetRealOrderPayStatusResult(C_0x8031.Resp resp) { 
	        
			if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
				//成功

			}else{
				//失败
                            
			} 	

	    }

 
	}
######请求下单（生成预付单）
【示例代码】
	
	//设置监听
	PersistentEventDispatcher.getInstance().registerOnPushListener(listener); 

	//发送请求  
	C_0x8028.Req.ContentBean req = new C_0x8028.Req.ContentBean(Type.ThirdPayment.TYPE_ORDER, Type.ThirdPayment.PLATFOME_WECHAT, currentOrder.getNo(), "INCHARGER-订单支付", "CNY", currentOrder.getNeed_pay_fee() + "");
	StartAI.getInstance().getBaseBusiManager().thirdPaymentUnifiedOrder(req,onCallListener)


	//实现业务处理回调
	AOnStartaiMessageArriveListener listener = new AOnStartaiMessageArriveListener() {


	    @Override
	    public void onThirdPaymentUnifiedOrderResult(C_0x8028.Resp resp) { 
	        
			if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
				//成功

			}else{
				//失败
                            
			} 	

	    }

 
	}
 


### 部分请求参数实体结构 

>C_0x8028.Req.ContentBean 请求下单 请求数据 

成员 | 类型 | 描述 |必须 | 备注
:|
platform | int | 支付平台 | 1 | C_0x8028.PLATFOME_WECHAT(1)微信支付<br>   C_0x8028.PLATFOME_ALIPAY(2)支付宝支付<br>  
type | int |交易类型 | 1 |  C_0x8028.TYPE_DEPOSIT(1)押金充值<br> C_0x8028.TYPE_BALANCE(2)余额充值<br> C_0x8028.TYPE_ORDER(3)订单支付<br>
goods_description | String | 商品描述 | 1 | 显示在微信支付单 如 “充电宝-押金充值”
fee_type | String | 货币类型 | 1 | "CNY"
total_fee | int | 总金额 | 1 | 单位： 分
order_num | String | 订单号 | 1 | 
 

>C_0x8031.Resp.ContentBean 查询支付结果 返回数据

成员 | 类型 | 描述 |  备注
:|
platform | int | 支付平台 |   C_0x8031.PLATFORM_WECHAT(1)微信 <br>C_0x8031.PLATFORM_ALIPAY(2)支付宝 <br> C_0x8031.PLATFORM_SMALL_APP(3) 微信小程序 
userid | String | 用户id |  
openid | String | 用户在商户appid下的唯一id |  
is_subscribe | String | 是否订阅 |  
bank_type | String | 付款银行 | 
total_fee | String | 交易金额 | 单位：分  
out_trade_no | String | 商户订单号 | 
transaction_id | String | 微信/支付宝支付订单号 | 
trade_type | String | 交易类型 |  "APP"
time_end | String | 支付完成时间 | 2018-11-11 22:22:22
trade_state | String | 交易状态 |  核心字段<br> C_0x8031.TRADE_STATE_SUCCESS(SUCCESS)支付成功<br> C_0x8031.TRADE_STATE_REFUND（REFUND）转入退款<br> C_0x8031.TRADE_STATE_NOTPAY(NOTPAY)未支付<br> C_0x8031.TRADE_STATE_CLOSED(CLOSED)已关闭<br> C_0x8031.TRADE_STATE_REVOKED(REVOKED)已撤销（刷卡支付）<br> C_0x8031.TRADE_STATE_USERPAYING(USERPAYING)用户支付中<br> C_0x8031.TRADE_STATE_PAYERROR(PAYERROR)支付失败(其他原因，如银行返回失败)<br> C_0x8031.TRADE_STATE_ERROR(ERROR)错误 
coupon_fee | String | 代金券金额 | 单位：分
cash_fee | String | 现金支付金额 | 单位：分 
coupon_count | String | 代金券数量 | 单位：分 
trade_state_desc | String | 交易状态描述 | "支付成功"
 
   
>C_0x8002.Resp.ContentBean.BebindingBean 添加设备|好友成功返回结果

成员 | 类型 | 描述 | 备注
:|
id | String | 设备的sn或userid | 
apptype | String | 对方的类型 | 
featureid | String | 功能id |  
connstatus | int | 连接状态 | 1表示在线0表示离线
topic | String | 对端的topic | 在点对点发消息时需要携带此参数  

>C_0x8018.Resp.ContentBean 登录成功返回结果

成员 | 类型 | 描述 | 备注
:|
userid | String | 用户id | 
token | String | 用户token | 
uname | String | 用户名 | email/mobile/username
type | int | 登录类型 | 1表示邮箱加密码 2表示手机号加密码 3表示手机号加验证码 4表示用户名加密码 5.双重认证 手机号+验证码+密码
expire_in | long | token时效 | 单位 秒  

>C_0x8005.Resp.ContentBean 查询好友|设备列表成功结果 

成员 | 类型 | 描述 | 备注
:|
apptype | String | 设备类型 | 
bindingtime | long | 绑定时间 | 
id | String | sn或userid |   
type | int | 好友类型 |  1.查询用户绑定的设备2.查询设备的用户列表3.查询用户的用户好友4.查询设备的设备列表5.查询用户的手机列表6.查询手机的用户好友7.查询所有
alias | String | 别名 | 如果没有设置默认为对方的sn或suerid
topic | String | 对端的topic | 在点对点发消息时需要携带此参数  

>C_0x8024.Resp.ContentBean 查询用户信息返回实体

成员 | 类型 | 描述  |备注
:|
userid | String | 用户id  | 
userName | String | 登录用户名  |  
birthday | String | 生日  | 格式 1991-12-11
province | String | 省  |  
city | String | 市  |  
town | String | 区  | 
address | String | 详细地址  |  
nickName | String | 昵称  | 
headPic | String | 头像  | http://file.startai.com.cn/aaaa.jpg
sex | String | 性别 |    男/女
firstName | String | 名  |  
lastName | String | 姓  |  
isHavePwd | int | 是否已经设置过登录密码  |  1为已设置
mobile | String | 手机号  |   
email | String | 邮箱号  |   
注：在请求时，如果不想修改此字段请将无需填写,服务器会自动保存上次的数据。
 
>C_0x8020.Req.ContentBean 修改用户信息请求实体

成员 | 类型 | 描述 | 必填 |备注
:|
userid | String | 用户id | 1 | 
userName | String | 登录用户名 | 0 | 修改后不可再次更改
birthday | String | 生日 | 0 | 格式 1991-12-11
province | String | 省 | 0 |  
city | String | 市 | 0 |  
town | String | 区 | 0 | 
address | String | 详细地址 | 0 |  
nickName | String | 昵称 | 0 | 
headPic | String | 头像 | 0 | http://file.startai.com.cn/aaaa.jpg
sex | String | 性别 |  0 |  男/女
firstName | String | 名 | 0 |  
lastName | String | 姓 |  0 |  
注：在请求时，如果不想修改此字段请将无需填写,服务器会自动保存上次的数据。

 

>MqttPublishRequest < RequestType > 消息发送请求实体

成员 | 类型 | 描述 | 备注
:|
topic | String | 消息发送的主题 | 
qos | int | 消息质量 | 默认为1 
RequestType | 泛型 | 要发送的内容 |  String 或 StartaiMessage 

>StartaiMessage 消息内容

成员 | 类型 | 描述  | 必填| 备注
:|
msgcw | String | 消息方向 | 1 |  0x01 端到端发起单次设置 0x02 结果单次返回 0x03 端到端发起周期设置 0x04 结果周期返回或事件上报  0x05 端到端发起查询 0x06 查询结果返回 0x07 终端向云端发起事件 0x08	云端事件返回终端 0x09 云端向终端发起事件 0x10 终端事件返回云端 0x12连接事件 alarm 终端告警上报
msgtype | String | 消息类型  | 1|  
result | int | 结果 | 0 |  
m_ver | String | 协议版本  | 1|  示例：Json/Tlv_1.2.6_9.2.1
msgid | String | 消息id  | 0|   
ts | long | 时间  | 1|  单位 毫秒
appid | String | appid  | 1|  开发者平台申请  
domain | String | 域  | 0|  开发者平台申请  
toid | String | 消息接收方 | 1 |   
fromid | String | 消息发送方  | 1|   
content | Object | 具体的内容  | 1|   不同的消息类型，content格式不一样

 

### 注意事项 
- needUISafety（） 方法返回true表示此接口的其他方法都将回调到主线程，fase则相反。
- sdk首次初始化成功后会回调到 onActiviteResult()。只有此接口回调成功才可以正常收发消息。 
- 开发者可以选择在封装好的业务回调中处理业务，也可以在onCommand(topic,cmd)事件中处理业务。

 
### 异常码
 
异常码 | 描述 
:|
3000 | 未知异常 
4001 | 连接失败，网络异常
4002 | 连接失败，认证失败 
4003 | 连接失败，密钥无效 
4004 | 连接失败，超时
4005 | 连接失败，服务器异常
4006 | 连接失败，未知异常
5001 | 发送失败，未连接
5002 | 发送失败，超时
5003 | 发送失败，未知异常
5004 | 发送失败，网络异常
5006 | 发送失败，未登录
5007 | 发送失败，未激活
5008 | 发送失败，参数非法
5009 | 发送失败，不存在此好友 
5010 | 发送失败，主题包含乱码
5011 | 发送失败，mqtt未连接
5012 | 发送失败，当前网络不可用
5013 | 发送失败，当前网络类型不是wifi
5014 | 发送失败，与设备不在同一局域网
5015 | 发送失败，发送类型不对
5016 | 发送失败，只支持局域网通信
5017 | 发送失败，未正确初始化sdk
5109 | 发送失败，设备未在广域网绑定
5110 | 发送失败，设备未在局域网绑定
6001 | 连接断开，账号在别处登录 
6002 | 连接断开，mqtt断开连接
6003 | 连接断开，超时
6004 | 连接断开，心跳超时
6005 | 连接断开，主动断开
6006 | 接连断开，网络不可用
7001 | 订阅失败，空主题
7002 | 订阅失败，主题格式非法
8001 | 取消订阅失败，空主题
8002 | 取消订阅失败，主题格式非法
1 | 无效协议版本
2 | 无效客户机标识
3 | 代理程序不可用
4 | 错误的用户名或密码
5 | 无权连接
6 | 意外错误
32000 | 等待来自服务器的响应时超时
32100 | 已连接客户机
32101 | 已断开客户机连接
32102 | 客户机正在断开连接
32103 | 无法连接至服务器
32104 | 客户机未连接
32105 | 指定的 SocketFactory 类型与代理程序 URI 不匹配
32106 | SSL 配置错误
32107 | 不允许通过回调方法断开连接
32108 | 不可识别的包
32109 | 已断开连接
32110 | 已在进行连接
32111 | 客户机已关闭
32200 | 持久性已在使用中
32201 | 令牌已在使用中
32202 | 正在进行过多的发布
00000000 | 未知异常
0x800101 | 终端激活失败,Content格式有误
0x800102 | 终端激活失败,参数丢失
0x800103 | 	终端激活失败,sn格式不正确
0x800104 | 	终端激活失败,设备不合法
0x800105 | 	终端激活失败,数据保存失败
0x800106 |	代激活失败, 参数丢失
0x800107 |	代激活失败, sn格式不正确
0x800108 |	代激活失败,设备不合法
0x800109 |	代激活失败,数据保存失败
0x800201 | 	加好友失败,content格式有误
0x800202 | 	加好友失败,参数丢失
0x800203 | 	加好友失败,被绑定id无效
0x800204 | 	加好友失败,绑定id无效
0x800205 | 	加好友失败,无需重复添加
0x800206 |  加好友失败,数据保存失败
0x800207 |	加好友失败,不允许跨域绑定
0x800301 |	注销设备失败,content格式有误
0x800302 |	注销设备失败,参数丢失
0x800303 |	注销设备失败id无效
0x800304 |	数据更改失败
0x800401 | 	解除好友关系失败,content格式有误
0x800402 | 	解除好友关系失败,参数丢失
0x800403 | 	解除好友关系失败,数据库删除失败1
0x800404 |  解除好友关系失败,不允许移除
0x800501 | 	查询好友列表失败,content格式有误
0x800502 | 	查询好友列表失败,参数丢失
0x800503 |	查询好友列表失败,类型不支持
0x801501 | 	更改好友备注失败,content格式有误
0x801502 | 	更改好友备注失败
0x801503 | 	更新好友备注名失败
0x801601 | 	获取软件最新版本号失败,content格式有误
0x801602 | 	获取软件最新版本号失败,参数丢失
0x801603 | 	获取软件最新版本号失败,os参数不支持
0x801604 | 	没有最新版本
0x801701 |	用户注册失败,content格式有误
0x801702 |	用户注册失败,参数丢失
0x801703 |	用户注册失败,密码格式有误
0x801704 |	用户注册失败,手机号码或邮箱格式有误
0x801705 |	用户注册失败,手机号已经注册
0x801706 |	用户注册失败,注册类型不支持
0x801707 |	用户注册失败,数据保存失败
0x801708 |	用户注册失败,邮件发送失败
0x801709 |  用户注册失败,Appid无效
0x801801 |	用户登录失败,content格式有误
0x801802 |	用户登录失败,参数丢失
0x801803 |	用户登录失败,登录类型不支持
0x801804 |	用户登录失败,尚未注册
0x801805 |	用户登录失败,邮箱未激活
0x801806 |	用户登录失败,密码错误
0x801807 |	用户登录失败,验证码错误或过期
0x801808 |	用户登录失败,数据保存失败
0x801809 |	用户登录失败,该用户已长期未登陆
0x801810 |  用户登录失败,未设置密码,请使用手机+验证码登录
0x801811 |  用户登录失败,appid无效
0x801901 | 	更新设备信息失败,content格式有误
0x801902 | 	更新设备信息失败,参数丢失
0x802001 |	更新用户信息失败,Content格式有误
0x802002 |	更新用户信息失败,参数丢失
0x802003 |	更新用户信息失败,userid无效
0x802004 |	更新用户信息失败,用户名不能更改
0x802005 |	更新用户信息失败,用户名已存在
0x802006 |	更新用户信息失败,数据保存失败
0x802101 | 	获取手机验证码失败,content格式有误
0x802102 |  获取手机验证码失败,参数丢失
0x802103 | 	获取手机验证码失败,手机号格式不正确
0x802104 | 	获取手机验证码失败,类型不支持
0x802105 | 	获取手机验证码失败,无需重复注册
0x802106 | 	获取手机验证码失败,验证码发送失败
0x802107 |	获取手机验证码失败,数据保存失败
0x802108 |	获取手机验证码失败,appid无效
0x802109 |	获取手机验证码失败,未设置过密码,不可更改密码
0x802201 | 	校验验证码失败,content格式有误
0x802202 | 	校验验证码失败,参数丢失
0x802203 | 	校验验证码失败,手机号格式不正确
0x802204 | 	校验验证码失败,类型不支持
0x802205 | 	校验验证码失败,验证码错误
0x802206 | 	校验验证码失败,验证码过期
0x802207 |	校验验证码失败,appid无效
0x802301 |	发送邮件失败,content格式有误
0x802302 |	发送邮件失败,参数丢失
0x802303 |	发送邮件失败,该邮箱尚未注册
0x802304 |	发送邮件失败,已经激活,无需重新激活
0x802305 |	发送激活邮件失败
0x802306 |	发送邮件失败,Appid不合法
0x802307 |	发送邮件失败,请先激活
0x802308 |	发送邮件失败,类型不支持
0x802401 |	获取用户信息失败,content格式有误
0x802402 |	获取用户信息失败,参数丢失
0x802403 |	获取用户信息失败,userid不合法
0x802501 |	更改密码失败,content格式有误
0x802502 |	更改密码失败,参数丢失
0x802503 |	更改密码失败,用户id不合法
0x802504 |	更改密码失败,旧密码不合法
0x802505 |	更改密码失败,数据保存失败
0x802506 |	更改密码失败,密码格式错误
0x802601 |	重置密码失败,content格式有误
0x802602 |	重置密码失败,参数丢失
0x802603 |	重置密码失败,手机号码无效
0x802604 |	重置密码失败,尚未设置密码
0x802605 |	重置密码失败,数据保存失败
0x802606 |	重置密码失败,验证信息已过期
0x802607 |	重置密码失败,appid无效
0x802608 |	重置密码失败,密码格式有误
0x802701 |	第三方登录失败,content格式有误
0x802702 |	第三方登录失败,参数丢失
0x802703 |	第三方登录失败,登录类型不支持
0x802704 |	第三方登录失败,尚未设置第三方登录参数
0x802705 |	第三方登录失败,appid无效
0x802706 |	第三方登录失败,数据保存失败
0x802707 |	第三方登录失败,获取微信token失败
0x802708 |	第三方登录失败,获取用户信息失败
0x802801 |	支付失败,content格式有误
0x802802 |	支付失败,格式不支持
0x802803 |	支付失败.参数丢失
0x802804 |	支付失败.金额格式不正确
0x802805 |	没有设置第三方支付参数
0x802806 |	与第三方支付平台通讯失败
0x802807 |	平台数据保存失败
0x802808 |	支付失败,appid 无效
0x803101 |	查询支付结果失败,content格式有误
0x803102 |	查询支付结果失败,参数丢失
0x803103 |	查询支付结果失败,appid无效
0x803104 |	查询支付结果失败,订单无效
0x803105 |	查询支付结果失败,与支付平台通讯失败
0x803106 |	查询支付结果失败,平台保存数据失败
0x803201 |	查询平台id失败,content有误
0x803202 |	查询平台id失败,参数丢失
0x803301 |	参数丢失
0x803302 |	Appid无效
0x803401 |	添加/更换绑定手机失败,content格式有误
0x803402 |	参数丢失
0x803403 |	手机格式有误
0x803404 |	不需重复绑定
0x803405 |	该手机已被其他用户绑定
0x803406 |	验证信息已过期
0x803407 |	数据保存失败
0x803408 |	appid不合法
0X803501 |	获取天气信息失败,content格式有误
0X803502 |	参数丢失
0x803503 |	请求天气接口失败
0x803504 |	该城市/地区没有你所请求的数据
0x803505 |	未知或错误城市/地区
0x803506 |	超过当天访问次数
0x803507 |	超过每分钟的访问次数, 
0X803601 |	解绑第三方账号失败,content格式有误
0x803602 |	参数丢失
0x803603 |	解绑type无效
0x803604 |	userid无效
0x803605 |	至少保留一个账号
0x803606 |	没有绑定该类型的账号
0x803607 |	数据库操作失败
0x803701 |	绑定第三方账号失败,contet格式有误
0x803702 |	参数丢失
0x803703 |	绑定账号类型不支持
0x803704 |	没有设置绑定第三方账号参数
0x803705 |	获取微信token失败
0x803706 |	获取微信用户信息失败
0x803707 |	获取支付宝用户信息失败
0x803708 |	appid不合法
0x803709 |	userid不合法
0x803710 |	不用重复添加
0x803711 |	该账号已被其他用户绑定


###更新日志

- 2018-12-27 v3.1.31
		
		1，添加绑定，解绑第三方账号接口及回调处理。
		2，添加更改/绑定手机号接口及回调处理。
		3，添加获取支付宝授权信息接口及回调处理。
		4，添加请求下单接口及回调处理。
		5，添加真实查询支付结果及回调处理。
		6，添加设置消息返回是否分发到同一个用户的其他手机。
		7，补全异常码。

- 2018-11-27 v3.1.27

		1，取消发送上线事件，取消遗嘱设定。
		2，调整sn生成规则,添加appid，以确保每个app所生成的sn唯一。
		3，修复同一瞬间发送大量消息时提示“正在进行过多的发布”的bug。

- 2018-11-22 v3.1.25

		1，添加mqtt对设备的订阅 Q/clienti/{sn}-A。
		2，调整sn长度为16位。
	
- 2018-11-09 v3.1.21
		
		1，修复在同一台手机上两个app分别初始化两个mqsdk，两个app之前的接收消息会互相干扰的bug。
		2，添加|完善部分异常码。
		

- 2018-09-05 v3.1.15
 
		1，修复当在子线程中初始化sdk时，needUISafety()设置成true，业务处理结果仍会回调到子线程的bug。 

- 2018-09-04 v3.1.14
		
		1，修复在微信登录状态调用正常用户登录接口时sdk闪退的BUG。
		2，调整登录时效当expire_in = -1时，时效无限期。
		3，调整第三登录由8303 => 8027 。


- 2018-09-03 v3.1.12

		1，调整业务处理回调接口结构。
		2，修复因sn乱码导致连接异常断开的BUG。
		3，添加第三方登录api及结果回调。
		4，修复在初始化两个本地broke后当默认的连接不上时无法自动切换到下一个broke的bug。


- 2018-08-28 v3.0.5

		1，调整获取验证码返回结果回调。
		2，修复因登录失败时无法返回登录类型的bug。
		3，修复切换用户登录时，登录用户的信息跟本地保存的用户信息不匹配的bug。


- 2018-08-07 v3.0.4
		
		
		1，添加消息发送异常码5007，5008。
		2，添加修改密码api及结果回调。
		3，添加发送邮件api及结果回调。
		4，添加修改备注名api及结果回调。
		5，添加获取最新app版本信息api及结果回调。
		6，添加查询用户信息api及结果回调。
		7，添加更新用户信息api及结果回调。 
		8，新增一批异常码0x8023..--0x8025..。
		9，修复在没有网络的情况下连接状态没有回调到应用层的bug。

- 2018-07-25 v3.0.3

		1，添加sdk层对发送消息的参数合法性校验，如果参数非法会回调至应用层。
		2，调整注册返回的结果回调，添加注册类型。

- 2018-07-18 v3.0.2

		1，添加设备注销激活api及消息处理结果回调。
		2，添加替第三方智能硬件激活api及结果回调。
        3，修复网络判断偶尔会有误的bug。
		4，修改登录结果回调解绑结果回调绑定结果回调的数据。扩展id。
	 	5，修复当获取区域节点数据为空时，终端不会重连的bug。
		6，添加sdk对透传消息包含空格的处理。
		7，添加更新用户信息api接口及消息处理结果回调。
		8，添加查询用户信息api接口及消息处理结果回调。
		9，添加智能设备上线下线状态监听回调。


- 2018-07-03 v3.0.1

		1，完成10个基本业务的封装及通用的消息收发接口。
		2，添加部分异常码。
