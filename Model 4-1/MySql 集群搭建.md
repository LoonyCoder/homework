### **MySql** 集群搭建

一、环境软件版本

| 软件                 | **版本**    |
| -------------------- | ----------- |
| 虚拟机 VMware Fusion | 15          |
| 服务器 CentOS        | 7.x         |
| 数据库 Mysql         | 5.7.28      |
| SSH工具              | ZenTermLite |

二、环境架构介绍

| 主机（hostname） | ip             | 角色        | 权限         |
| ---------------- | -------------- | ----------- | ------------ |
| Centos-7-master  | 172.16.240.132 | Mysql Slave | 可读写、主库 |
| Centos-7-slave-1 | 172.16.240.133 | Mysql Slave | 只读、从库   |
| Centos-7-slave-2 | 172.16.240.134 | Mysql Slave | 只读、从库   |
| Centos-7-mha     | 172.16.240.135 | MHA Manager | 高可用监控   |

三、搭建

下载

```bash
wget https://cdn.mysql.com/archives/mysql-5.7/mysql-5.7.28-1.el7.x86_64.rpm-bundle.tar
```

解压

```bash
tar xvf mysql-5.7.28-1.el7.x86_64.rpm-bundle.tar
mysql-community-embedded-5.7.28-1.el7.x86_64.rpm
mysql-community-libs-compat-5.7.28-1.el7.x86_64.rpm
mysql-community-devel-5.7.28-1.el7.x86_64.rpm
mysql-community-embedded-compat-5.7.28-1.el7.x86_64.rpm
mysql-community-libs-5.7.28-1.el7.x86_64.rpm
mysql-community-test-5.7.28-1.el7.x86_64.rpm
mysql-community-common-5.7.28-1.el7.x86_64.rpm
mysql-community-embedded-devel-5.7.28-1.el7.x86_64.rpm
mysql-community-client-5.7.28-1.el7.x86_64.rpm
mysql-community-server-5.7.28-1.el7.x86_64.rpm
```

安装

**要移除CentOS自带的mariadb-libs，不然会提示冲突**

```bash
rpm -qa|grep mariadb //查看你本机的mariadb安装包文件
rpm -e mariadb-libs-5.5.41-2.el7_0.x86_64 --nodeps  //卸载mariadb
```

由于MySQL的server服务依赖了common、libs、client，所以需要按照以下顺序依次安装。 RPM是Red Hat公司随Redhat Linux推出的一个软件包管理器，通过它能够更加方便地实现软件的安装。

rpm常用的命令有以下几个：

```ba
-i, --install 安装软件包
-v, --verbose 可视化，提供更多的详细信息的输出 -h, --hash 显示安装进度
-U, --upgrade=<packagefile>+ 升级软件包
-e, --erase=<package>+ 卸载软件包
--nodeps 不验证软件包的依赖
```

组合可得到几个常用命令：

```bash
安装软件:rpm -ivh rpm包名
升级软件:rpm -Uvh rpm包名
卸载软件:rpm -e rpm包名
查看某个包是否被安装 rpm -qa | grep 软件名称
```

下面就利用安装命令来安装mysql：

```bash
rpm -ivh mysql-community-common-5.7.28-1.el7.x86_64.rpm
rpm -ivh mysql-community-libs-5.7.28-1.el7.x86_64.rpm
rpm -ivh mysql-community-libs-compat-5.7.28-1.el7.x86_64.rpm
rpm -ivh mysql-community-client-5.7.28-1.el7.x86_64.rpm
rpm -ivh mysql-community-server-5.7.28-1.el7.x86_64.rpm
rpm -ivh mysql-community-devel-5.7.28-1.el7.x86_64.rpm
```

**启动**

初始化用户

```ba
mysqld --initialize --user=mysql
```

查看初始密码

```bash
cat /var/log/mysqld.log | grep password
```

启动mysql服务

```bash
systemctl start mysqld
```

配置为开机启动

```bash
systemctl enable mysqld
```

接下来修改默认密码。进入mysql console

```bash
mysql -uroot -p
mysql> SET PASSWORD = PASSWORD('root');
Query OK, 0 rows affected, 1 warning (0.00 sec)
```

四、关闭防火墙

不同的MySQL直接要互相访问，需要关闭Linux的防火墙，否则就要在配置/etc/sysconfig/iptables中增

加规则。配置防火墙不是本次教程的重点，所以四台服务器均关闭防火墙。

```b
systemctl stop firewalld
如果是iptables的话那么执行
systemctl stop iptables
```

五、MySQL主从配置

**修改Master配置文件**

```bash
vim /etc/my.cnf

#bin_log配置
log_bin=mysql-bin  #开启binlog
server-id=1        #server id标识
sync-binlog=1
#配置忽略的db
binlog-ignore-db=information_schema 
binlog-ignore-db=mysql 
binlog-ignore-db=performation_schema 
binlog-ignore-db=sys

#relay_log配置 
relay_log=mysql-relay-bin
log_slave_updates=1
relay_log_purge=0 

#gtid配置
#开启gtid
#gtid_mode=on #enforce_gtid_consistency=1
```

**重启服务**

```bash
systemctl restart mysqld
```

**主库给从库授权**

```ba
grant replication slave on *.* to root@'%' identified by 'root';
grant all privileges on *.* to root@'%' identified by 'root';
flush privileges;

//查看主库master_log_file='mysql- bin.000007',master_log_pos=154
show master status;
```

**Slave** **节点**

**Ps:我这里是修改Centos-7-slave-1，Centos-7-slave-2这两台节点的mysql配置**

修改Slave配置文件，两台Slave的server-id分别设置为2和3，注意这个地方如果你也是通过复制整个虚拟机pvm的方式构建的节点的话，那么需要

```bash
rm -rf /var/lib/mysql/auto.cnf //因为里面uuid是一样的，启动从节点mysql的时候会报错，删除该文件在启动mysql服务的时候该文件会自动创建，并且赋予随机的uuid
```

**slave节点配置如下**

```bash
vim /etc/my.cnf

#bin_log配置 
log_bin=mysql-bin 
#服务器ID,从库1是2,从库2是3
server-id=2
sync-binlog=1 
binlog-ignore-db=information_schema 
binlog-ignore-db=mysql 
binlog-ignore-db=performation_schema 
binlog-ignore-db=sys
#relay_log配置 
relay_log=mysql-relay-bin 
log_slave_updates=1 
relay_log_purge=0
read_only=1
#gtid配置
#开启gtid
#gtid_mode=on #enforce_gtid_consistency=1
```

**启动Slave Mysql服务**

```bash
systemctl start mysqld
```

**开启同步**

在Slave节点的**MySQL命令行**执行如下命令:

```bash
mysql> change master to master_host='172.16.240.138',master_port=3306,master_user='root',master_password
='root',master_log_file='mysql-bin.000009',master_log_pos=2086; //需要在主节点mysql命令行通过 show master status; 获得

start slave; // 开启同步

```

**六、配置半同步复制**

**Master节点**
**安装插件**

```bash
install plugin rpl_semi_sync_master soname 'semisync_master.so';
show variables like '%semi%';
```

**修改配置文件**

```bash
vim /etc/my.cnf

# 自动开启半同步复制 
rpl_semi_sync_master_enabled=ON 
rpl_semi_sync_master_timeout=1000
```

**重启Master服务**

```bash
systemctl restart mysqld
```

**Slave** **节点**

两台Slave节点都执行以下步骤。

**安装插件**

```bash
install plugin rpl_semi_sync_slave soname 'semisync_slave.so';
```

**修改配置文件**

```bash
vim /etc/my.cnf
# 自动开启半同步复制 
rpl_semi_sync_slave_enabled=ON
```

**重启服务**

```bash
systemctl restart mysqld
```

**测试半同步状态**

首先通过MySQL命令行检查参数的方式，查看半同步是否开启。

```bash
show variables like '%semi%'
```

Master节点：

![image-20200730002018215](/Users/gmx/Library/Application Support/typora-user-images/image-20200730002018215.png)

Slave节点：

![image-20200730002050309](/Users/gmx/Library/Application Support/typora-user-images/image-20200730002050309.png)

然后通过MySQL日志再次确认。

```bash
cat /var/log/mysqld.log
可以看到日志中已经启动半同步:
Start semi-sync binlog_dump to slave (server_id: 2), pos(mysql-bin.000002, 154)
```

**七、MHA** **高可用搭建**

在四台服务器上分别执行下面命令，生成公钥和私钥，换行回车采用默认值

```bash
ssh-keygen -t rsa
```

在三台MySQL服务器分别执行下面命令，将公钥拷到MHA Manager服务器上

```ba
ssh-copy-id 172.16.240.132
```

之后可以在MHA Manager服务器上检查下，看看.ssh/authorized_keys文件是否包含3个公钥

```bash
cat /root/.ssh/authorized_keys
```

从MHA Manager服务器执行下面命令，向其他三台MySQL服务器分发公钥信息。

Ps:如果下面命令不成功可以使用ssh-copy-id一台台的copy

```bash
scp /root/.ssh/authorized_keys 172.16.240.138:$PWD
scp /root/.ssh/authorized_keys 172.16.240.136:$PWD
scp /root/.ssh/authorized_keys 172.16.240.137:$PWD
```

可以MHA Manager执行下面命令，检测下是否实现ssh互通

```bash
ssh centos-7-master
```

**MHA** **下载安装**

**MHA** **下载**

MySQL5.7对应的MHA版本是0.5.8，所以在GitHub上找到对应的rpm包进行下载，MHA manager和 node的安装包需要分别下载:

<https://github.com/yoshinorim/mha4mysql-manager/releases/tag/v0.58>

<https://github.com/yoshinorim/mha4mysql-node/releases/tag/v0.58>

下载后，将Manager和Node的安装包分别上传到对应的服务器。(可使用WinSCP等工具)

- 三台MySQL服务器需要安装node
- MHA Manager服务器需要安装manager和node

提示:也可以使用wget命令在linux系统直接下载获取，例如

```bash
wget https://github.com/yoshinorim/mha4mysql-manager/releases/download/v0.58/mha4mysql-manager-0.58-0.el7.centos.noarch.rpm
```

**MHA node安装**
在四台服务器上安装mha4mysql-node。 MHA的Node依赖于perl-DBD-MySQL，所以要先安装perl-DBD-MySQL。

```bash
yum install perl-DBD-MySQL -y

rpm -ivh mha4mysql-node-0.58-0.el7.centos.noarch.rpm
```

**MHA manager安装**

在MHA Manager服务器安装mha4mysql-node和mha4mysql-manager。

MHA的manager又依赖了perl-Config-Tiny、perl-Log-Dispatch、perl-Parallel-ForkManager，也分别 进行安装。

```bash
wget http://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm
rpm -ivh epel-release-latest-7.noarch.rpm

yum install perl-DBD-MySQL perl-Config-Tiny perl-Log-Dispatch perl-Parallel-
ForkManager -y

wget https://github.com/yoshinorim/mha4mysql-
node/releases/download/v0.58/mha4mysql-node-0.58-0.el7.centos.noarch.rpm

rpm -ivh mha4mysql-node-0.58-0.el7.centos.noarch.rpm

wget https://github.com/yoshinorim/mha4mysql-
manager/releases/download/v0.58/mha4mysql-manager-0.58-0.el7.centos.noarch.rpm

rpm -ivh mha4mysql-manager-0.58-0.el7.centos.noarch.rpm
```

提示:由于perl-Log-Dispatch和perl-Parallel-ForkManager这两个被依赖包在yum仓库找不到， 因此安装epel-release-latest-7.noarch.rpm。在使用时，可能会出现下面异常:**Cannot retrieve metalink for repository: epel/x86_64**。可以尝试使 用/etc/yum.repos.d/epel.repo，然后注释掉mirrorlist，取消注释baseurl。

**MHA配置文件**

MHA Manager服务器需要为每个监控的 Master/Slave 集群提供一个专用的配置文件，而所有的

Master/Slave 集群也可共享全局配置。

在mha这台机器上：

**初始化配置目录**

```bash
mkdir -p /var/log/mha/app1 (MHA监控实例根目录)
touch /var/log/mha/app1/manager.log (MHA监控实例日志文件)
```

**配置监控全局配置文件**

```bash
vim /etc/masterha_default.cnf

[server default]
#用户名
user=root
#密码
password=root
#ssh登录账号
ssh_user=root
#主从复制账号
repl_user=root
#主从复制密码
repl_password=root
#ping次数
ping_interval=1
#二次检查的主机
secondary_check_script=masterha_secondary_check -s 172.16.240.132 -s 172.16.240.133 -s 172.16.240.134
```

**配置监控实例配置文件**

```bash
vim /etc/mha/app1.cnf

[server default]
#MHA监控实例根目录 manager_workdir=/var/log/mha/app1 #MHA监控实例日志文件 manager_log=/var/log/mha/app1/manager.log
#[serverx] 服务器编号 #hostname 主机名 #candidate_master 可以做主库 #master_binlog_dir binlog日志文件目录
[server1]
hostname=172.16.240.132
candidate_master=1
master_binlog_dir="/var/lib/mysql"

[server2]
hostname=172.16.240.133
candidate_master=1
master_binlog_dir="/var/lib/mysql"

[server3]
hostname=172.16.240.134
candidate_master=1
master_binlog_dir="/var/lib/mysql"
```

**MHA配置检测**

**执行ssh通信检测**

在MHA Manager服务器上执行:

```bash
masterha_check_ssh --conf=/etc/mha/app1.cnf
```

如下图表示ssh互通成功

![image-20200730002737722](/Users/gmx/Library/Application Support/typora-user-images/image-20200730002737722.png)

**检测MySQL主从复制**

在MHA Manager服务器上执行:

```bash
masterha_check_repl --conf=/etc/mha/app1.cnf
```

查看监控状态命令如下:

```bash
masterha_check_status --conf=/etc/mha/app1.cnf
```

查看监控日志命令如下:

```bash
tail -f /var/log/mha/app1/manager.log
```

**测试MHA故障转移**

**模拟主节点崩溃**

在MHA Manager服务器执行打开日志命令:

```bash
tail -200f /var/log/masterha/app1/app1.log
```

关闭Master MySQL服务器服务，模拟主节点崩溃

```bash
systemctl stop mysqld
```

查看MHA日志，可以看到哪台slave切换成了master