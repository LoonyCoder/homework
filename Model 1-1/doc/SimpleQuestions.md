##### 1、Mybatis动态sql是做什么的？都有哪些动态sql？简述一下动态sql的执行原理？
答：
- 动态 sql 用于在 xml 配置文件中可以完成 sql 语句的判断逻辑和动态拼接
- mybatis 提供了9种动态 sql ：
    - <code>&lt;if&gt;&lt;/if&gt;</code>
    - <code>&lt;where&gt;&lt;/where&gt;</code>
    - <code>&lt;choose&gt;&lt;/choose&gt;</code>
    - <code>&lt;when&gt;&lt;/when&gt;</code>
    - <code>&lt;otherwise&gt;&lt;/otherwise&gt;</code>
    - <code>&lt;trim&gt;&lt;/trim&gt;</code>
    - <code>&lt;set&gt;&lt;/set&gt;</code>
    - <code>&lt;foreach&gt;&lt;/foreach&gt;</code>
    - <code>&lt;bind&gt;&lt;/bind&gt;</code>
- 执行原理：
使用 ognl 的表达式，从 sql 参数对象中计算表达式的值，根据表达式的值动态拼接 sql 
    
---

##### 2、Mybatis是否支持延迟加载？如果支持，它的实现原理是什么？
答：
- mybatis 仅支持 association 关联对象和 collection 关联集合对象的延迟加载，association 指的就是一对一， collection 指的就是一对多查询。在 mybatis 配置文件中，可以配置是否启用延迟加载 <code>lazyLoadingEnabled=true|false</code>
- 实现原理：
使用 cglib 代理创建目标对象的代理对象，当调用目标方法时，进入拦截器方法，比如调用<code>a.getB().getName()</code>，拦截器<code>invoke()</code>方法发现<code>a.getB()</code>是 null 值，那么就会单独发送事先保存好的查询关联 B 对象的 sql ，把 B 查询上来，然后调用<code>a.setB(b)</code>，于是 a 的对象 b 属性就有值了，接着完成<code>a.getB().getName()</code>方法的调用。
---

##### 3、Mybatis都有哪些Executor执行器？它们之间的区别是什么？
答：
- mybatis 有三种基本的 Executor 执行器，SimpleExecutor、ReuseExecutor、BatchExecutor。
  
- 区别：
    - **SimpleExecutor**：每执行一次 update 或 select ，就开启一个 Statement 对象，用完立刻关闭 Statement 对象。
    - **ReuseExecutor**：执行 update 或 select ，以 sql 作为 key 查找 Statement 对象，存在就使用，不存在就创建，用完后，不关闭 Statement 对象，而是放置于 <code>Map<String, Statement></code> 内，供下一次使用。简言之，就是重复使用 Statement 对象。
    - **BatchExecutor**：执行 update（**没有select，JDBC批处理不支持 select**），将所有 sql 都添加到批处理中（<code>addBatch()</code>），等待统一执行（<code>executeBatch()</code>），它缓存了多个 Statement 对象，每个 Statement 对象都是<code>addBatch()</code>完毕后，等待逐一执行<code>executeBatch()</code>批处理。与 JDBC 批处理相同。

---

##### 4、简述下Mybatis的一级、二级缓存（分别从存储结构、范围、失效场景。三个方面来作答）？
答:
- 一级缓存：mybatis 的一级缓存是指 SqlSession 级别的，作用域是 SqlSession ，mybatis 默认开启一级缓存，在同一个 SqlSession 中，相同的 sql 查询的时候，第一次查询的时候，就会从缓存中取，如果发现没有数据，那么就从数据库查询出来，并且缓存到 HashMap 中，如果下次还是相同的查询，就直接从缓存中查询，就不在去查询数据库，对应的就不在去执行 sql 语句。当查询到的数据，进行增删改的操作的时候，缓存将会失效。
  
- 二级缓存：二级缓存是 mapper 级别的缓存，多个 SqlSession 去操作同一个 mapper 的 sql 语句，多个 SqlSession 可以共用二级缓存，二级缓存是跨 SqlSession 。第一次调用 mapper 下的 sql 的时候去查询信息，查询到的信息会存放到该 mapper 对应的二级缓存区域，第二次调用 namespace 下的 mapper 映射文件中，相同的 sql 去查询，回去对应的二级缓存内取结果，如果在相同的 namespace 下的 mapper 映射文件中增删改，并且提交了失误，就会失效。
  
---

##### 5、简述Mybatis的插件运行原理，以及如何编写一个插件？
答：
mybatis 仅可以编写针对 ParameterHandler 、 ResultSetHandler 、 StatementHandler 、 Executor 这4种接口的插件，mybatis 使用 jdk 的动态代理，为需要拦截的接口生成代理对象以实现接口方法拦截功能，每当执行这4种接口对象的方法时，就会进入拦截方法，具体就是<code>InvocationHandler的invoke()</code>方法，当然，只会拦截那些你指定需要拦截的方法。
实现 mybatis 的<code>Interceptor</code>接口并重写<code>intercept()</code>方法，然后在给插件编写注解，指定要拦截哪一个接口的哪些方法即可，最后一定要在配置文件中配置插件！
  