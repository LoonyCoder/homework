集群架构 (三主三从)

```
master1  192.168.91.147:7001
master2  192.168.91.147:7002
master3  192.168.91.147:7003

slave1  192.168.91.147:7005
slave2  192.168.91.147:7006
slave3  192.168.91.147:7007

后面添加的节点
master4  192.168.91.147:7004
slave4  192.168.91.147:7008
```

安装相关依赖

```
yum install gcc-c++
yum install tcl
```

编译

```
mkdir /home/redis-cluster/master1
make install PREFIX=/home/redis-cluster/master1
```

修改 bin 目录下的redis.conf 配置文件

```
port 7001
cluster-enabled yes
daemonize yes
# bind 127.0.0.1
protected-mode no
pidfile /tmp/redis_7001.pid
```



复制多份redis , 例如

`cp -r /home/redis-cluster/master1  /home/redis-cluster/master2`

修改对应的配置文件， 端口号和pid文件名



依次启动各个redis实例

```
cd /home/redis-cluster/master1/bin
./redis-server redis.conf
cd /home/redis-cluster/master2/bin
./redis-server redis.conf
cd /home/redis-cluster/master3/bin
./redis-server redis.conf
cd /home/redis-cluster/slave1/bin
./redis-server redis.conf
cd /home/redis-cluster/slave2/bin
./redis-server redis.conf
cd /home/redis-cluster/slave3/bin
./redis-server redis.conf
```



启动集群

```
./redis-cli --cluster create 192.168.91.147:7001 192.168.91.147:7002 192.168.91.147:7003 192.168.91.147:7005 192.168.91.147:7006 192.168.91.147:7007 --cluster-replicas 1
```



`/redis-cli -h 127.0.0.1 -p 7001 -c`

扩容

```
./redis-cli --cluster add-node 192.168.91.147:7004 192.168.91.147:7001

./redis-cli --cluster reshard 192.168.91.147:7004

./redis-cli --cluster add-node 192.168.91.147:7008 192.168.91.147:7004 --cluster-slave --cluster-master-id a8b247bc671805deda17ea16fd4d363e16ff8ae1
```

