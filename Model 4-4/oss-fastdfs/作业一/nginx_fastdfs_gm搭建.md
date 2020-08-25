#### 目的

利用nignx, GraphicsMagick, fastdfs 实现动态缩略图的获取

#### 安装编译环境

`yum install git gcc gcc-c++ make automake vim wget libevent -y`

#### 安装libfastcommon 基础库

```
mkdir /root/fastdfs
cd /root/fastdfs
git clone https://github.com/happyfish100/libfastcommon.git --depth 1

cd libfastcommon/
./make.sh && ./make.sh install

```

测试上传

`/usr/bin/fdfs_upload_file /etc/fdfs/client.conf 1.png`

#### GraphicsMagick  安装

从官网上下载安装包GraphicsMagick-1.3.18.tar.gz

安装命令

```
tar -xvzf GraphicsMagick-1.3.18.tar.gz
cd GraphicsMagick-1.3.18
./configure
make && make install
```

可以用以下命令查看是否有相应图片类型的库, 若以下命令失败，并提示缺少相应解码器，可到GraphicsMagick 官网上下载并安装

`/usr/local/bin/gm identify 11.png`

若安装了图片库， 需要把共享库的地址（如 /usr/local/lib）加到 `/etc/ld.so.conf` 中



#### 安装相关依赖LuaJIT-2.0.4 ， ngx_devel_kit_v0.2.18

```
wget http://luajit.org/download/LuaJIT-2.0.4.tar.gz 
tar  -zxvf LuaJIT-2.0.4.tar.gz
cd  LuaJIT-2.0.4
./configure
make && make install PREFIX=/usr/local/LuaJIT

#添加环境变量
export LUAJIT_LIB=/usr/local/LuaJIT/lib
export LUAJIT_INC=/usr/local/LuaJIT/include/luajit-2.0

wget https://github.com/simpl/ngx_devel_kit/archive/v0.2.18.tar.gz
cd ngx_devel_kit_v0.2.18
./configure
make && make install
```

修改文件，/etc/ld.so.conf

加入 /usr/local/LuaJIT/lib

执行`ldconfig`

进入nginx安装包路径，重新编译nginx , 加入相关模块

```
./configure  --prefix=/usr/local/nginx --with-http_stub_status_module --add-module=/root/ngx_devel_kit-0.2.18 --add-module=/root/lua-nginx-module-0.8.10 --add-module=/root/fastdfs/fastdfs-nginx-module-1.20/src

make && make install
```

#### 测试lua

```
加入以下配置
	location /lua {
           default_type 'text/html';
           content_by_lua '
   		 ngx.say("hello, lua!")
  	   ';
	}

```

启动nignx `/usr/local/nginx/sbin/nginx`

编写脚本实现动态获取缩略图

```
        location /group1/M00 {
            default_type 'text/html';
            alias /home/fastdfs/data;
            set $image_root "/home/fastdfs/data";
            if ($uri ~ "/([a-zA-Z0-9]+)/([a-zA-Z0-9]+)/([a-zA-Z0-9]+)/([a-zA-Z0-9]+)/([a-zA-Z0-9]+)_([0-9]+)x([0-9]+)(.*)") {
            	set $image_dir "$image_root/$3/$4/";
                set $width $6;
                set $height $7;
                set $image_name "$5_$6x$7$8";
                set $file "$image_dir$image_name";
                set $org_file "$image_dir$5$8";
               
            }
            if (!-f $file) {
                lua_code_cache off;
                content_by_lua_file "/home/fastdfs.lua";
            }
        }

```

fastdfs.lua 内容如下

```
local command = "/usr/local/bin/gm convert " .. ngx.var.org_file .. " -thumbnail " .. ngx.var.width .. "x" .. ngx.var.height .. " " .. ngx.var.file;
os.execute(command);
ngx.exec(ngx.var.request_uri);

```

下载文件

http://192.168.91.139:8888/group1/M00/00/00/wKhbi19C0PmAJZACADmRpPwWyG0064_80x80.png