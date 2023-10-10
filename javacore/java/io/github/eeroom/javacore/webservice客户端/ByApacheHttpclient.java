package io.github.eeroom.javacore.webservice客户端;

import org.apache.http.protocol.HTTP;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ByApacheHttpclient {
    public static void main(String[] args) throws Throwable {
        doPost();

    }

    private static void doPost() throws Throwable {
        String url="http://localhost:49755/Home.asmx";
        HashMap<String,String> header=new HashMap<>();
        //header.put("Content-Type","text/xml; charset=utf-8");
        header.put("SOAPAction","\"http://tempuri.org/Seek\"");

        String parameterValue="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <TokenWraper xmlns=\"http://tempuri.org/\">\n" +
                "      <Jwt>abc123456</Jwt>\n" +
                "    </TokenWraper>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <Seek xmlns=\"http://tempuri.org/\">\n" +
                "      <st>\n" +
                "        <Age>101</Age>\n" +
                "        <Name>张三</Name>\n" +
                "      </st>\n" +
                "      <age>102</age>\n" +
                "    </Seek>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";

        /**
         * 使用CloseableHttpClient的工厂类HttpClients的方法来创建实例。HttpClients提供了根据各种默认配置来创建CloseableHttpClient实例的快捷方法。
         * 最简单的实例化方式是调用HttpClients.createDefault()。
         * 作者：小白豆豆5
         * 链接：https://www.jianshu.com/p/14c005e9287c/
         * 来源：简书
         * 简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
         */
        var httpclient0 = org.apache.http.impl.client.HttpClients.createDefault();

        /**
         * 使用CloseableHttpClient的builder类HttpClientBuilder，先对一些属性进行配置（采用装饰者模式，不断的.setxxxxx().setxxxxxxxx()就行了），再调用build方法来创建实例。
         * 上面的HttpClients.createDefault()实际上调用的也就是HttpClientBuilder.create().build()。
         * 其中需要注意的有 HttpCLientConnectionManager、HttpRoutePlanner 和 RequestConfig
         */
        var httpclientBuilder=org.apache.http.impl.client.HttpClientBuilder.create();

        /**
         * HttpClientConnectionManager是一个HTTP连接管理器。它负责新HTTP连接的创建、管理连接的生命周期还有保证一个HTTP连接在某一时刻只被一个线程使用。
         * 在内部实现的时候，manager使用一个ManagedHttpClientConnection的实例来作为一个实际connection的代理，负责管理connection的状态以及执行实际的I/O操作。
         * 如果一个被监管的connection被释放或者被明确关闭，尽管此时manager仍持有该连接的代理，但是这个connection的状态不会被改变也不能再执行任何的I/O操作。
         *
         * BasicHttpClientConnectionManager，每次只管理一个connection。不过，虽然它是thread-safe的，但由于它只管理一个连接，所以只能被一个线程使用。
         * 它在管理连接的时候如果发现有相同route的请求，会复用之前已经创建的连接，如果新来的请求不能复用之前的连接，它会关闭现有的连接并重新打开它来响应新的请求。
         *
         * PoolingHttpClientConnectionManager，与BasicHttpClientConnectionManager不同，它管理着一个连接池。
         * 它可以同时为多个线程服务。每次新来一个请求，如果在连接池中已经存在route相同并且可用的connection，连接池就会直接复用这个connection；
         * 当不存在route相同的connection，就新建一个connection为之服务；如果连接池已满，则请求会等待直到被服务或者超时。
         * 如果没有显式设置，默认每个route只允许最多2个connection，总的connection数量不超过20。
         * 这个值对于很多并发度高的应用来说是不够的，必须根据实际的情况设置合适的值，思路和线程池的大小设置方式是类似的，
         * 如果所有的连接请求都是到同一个url，那可以把MaxPerRoute的值设置成和MaxTotal一致，这样就能更高效地复用连接。
         *
         * 默认不对HttpClientBuilder进行配置的话，new出来的CloeableHttpClient实例使用的是PoolingHttpClientConnectionManager，
         * 这种情况下HttpClientBuilder创建出的HttpClient实例就可以被多个连接&多个线程共用，在应用容器起来的时候实例化一次，在整个应用结束的时候再调用httpClient.close()就行了
         */
        var connectionManager=new org.apache.http.impl.conn.PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(10);
        connectionManager.setDefaultMaxPerRoute(10);
        httpclientBuilder.setConnectionManager(connectionManager);

        /**
         * HttpRoutePlanner 一般不涉及
         * HttpClient不仅支持简单的直连、复杂的路由策略以及代理。
         * HttpRoutePlanner 是基于http上下文情况下，客户端到服务器的路由计算策略，一般没有代理的话，就不用设置这个东西。
         * 这里有一个很关键的概念—Route：在HttpClient中，一个Route指运行环境机器->目标机器host的一条线路，也就是如果目标url的host是同一个，那么它们的route也是一样的。
         */

        /**
         * RequestConfig是对request的一些配置。
         * 如果不设置request的Config，会在execute的过程中使用HttpClientParamConfig的getRequestConfig中用默认参数进行设置
         * 里面比较重要的有三个超时时间，默认的情况下这三个超时时间都为0，这也就意味着无限等待，很容易导致所有的请求阻塞在这个地方无限期等待。这三个超时时间为
         * connectionRequestTimeout—从连接池中取连接的超时时间
         * 这个时间定义的是从ConnectionManager管理的连接池中取出连接的超时时间， 如果连接池中没有可用的连接，则request会被阻塞，最长等待connectionRequestTimeout的时间，
         * connectTimeout—连接超时时间
         * 这个时间定义了通过网络与服务器建立连接的超时时间，也就是取得了连接池中的某个连接之后到接通目标url的连接等待时间
         * socketTimeout—请求超时时间
         * 这个时间定义了socket读数据的超时时间，也就是连接到服务器之后到从服务器获取响应数据需要等待的时间，或者说是连接上一个url之后到获取response的返回等待时间
         */
        var requestConfig = org.apache.http.client.config.RequestConfig.custom()
                .setConnectionRequestTimeout(10 * 1000)
                .setConnectTimeout(10 * 1000)
                .setSocketTimeout(10 * 1000)
                .build();
        httpclientBuilder.setDefaultRequestConfig(requestConfig);
        var httpclient=httpclientBuilder.build();

        /**
         * HttpClient支持所有的HTTP1.1中的所有定义的请求类型：GET、HEAD、POST、PUT、DELETE、TRACE和OPTIONS。
         * 对使用的类为HttpGet、HttpHead、HttpPost、HttpPut、HttpDelete、HttpTrace和HttpOptions。
         * Request的对象建立很简单，一般用目标url来构造就好了
         * 一个Request还可以addHeader、setEntity、setConfig等，一般这三个用的比较多。
         */
        var httpPost = new org.apache.http.client.methods.HttpPost(url);
        for (var kv:header.entrySet()){
            httpPost.setHeader(kv.getKey(),kv.getValue());
        }
        var urlEncodedFormEntityClass= org.apache.http.client.entity.UrlEncodedFormEntity.class;
        var httpClass= org.apache.http.protocol.HTTP.class;
        var contentType=org.apache.http.entity.ContentType.create("text/xml",StandardCharsets.UTF_8);
        var payload=new org.apache.http.entity.StringEntity(parameterValue,contentType);
        httpPost.setEntity(payload);
        /**
         * HttpReaponse是将服务端发回的Http响应解析后的对象。CloseableHttpClient的execute方法返回的response都是CloseableHttpResponse类型。
         * 可以getFirstHeader(String)、getLastHeader(String)、headerIterator（String）取得某个Header name对应的迭代器、getAllHeaders()、getEntity、getStatus等，一般这几个方法比较常用
         * 在这个部分中，对于entity的处理需要特别注意一下。
         * 一般来说一个response中的entity只能被使用一次，它是一个流，这个流被处理完就不再存在了。
         * 先response.getEntity()再使用HttpEntity#getContent()来得到一个java.io.InputStream，然后再对内容进行相应的处理。
         * 有一点非常重要，想要复用一个connection就必须要让它占有的系统资源得到正确释放。释放资源有两种方法：
         * a、关闭和entity相关的content stream，
         * 如果是使用outputStream就要保证整个entity都被write out，
         * 如果是inputStream，则再最后要记得调用inputStream.close()。或者使用EntityUtils.consume(entity)或EntityUtils.consumeQuietly(entity)来让entity被完全耗尽（后者不抛异常）来做这一工作。
         * EntityUtils中有个toString方法也很方便的（调用这个方法最后也会自动把inputStream close掉的），不过只有在可以确定收到的entity不是特别大的情况下才能使用。
         * 做过实验，如果没有让整个entity被fully consumed，则该连接是不能被复用的，很快就会因为在连接池中取不到可用的连接超时或者阻塞在这里（因为该连接的状态将会一直是leased的，即正在被使用的状态）。
         * 所以如果想要复用connection，一定一定要记得把entity fully consume掉，只要检测到stream的eof，是会自动调用ConnectionHolder的releaseConnection方法进行处理的
         * （注意，ConnectionHolder并不是一个public class，虽然里面有一些跟释放连接相关的重要操作，但是却无法直接调用）。
         * b、关闭response
         * 执行response.close()虽然会正确释放掉该connection占用的所有资源，但是这是一种比较暴力的方式，采用这种方式之后，这个connection就不能被重复使用了。
         * 从源代码中可以看出，response.close()调用了connectionHolder的abortConnection方法，它会close底层的socket，并且release当前的connection，并把reuse的时间设为0。
         * 这种情况下的connection称为expired connection，也就是client端单方面把连接关闭。还要等待closeExpiredConnections方法将它从连接池中清除掉
         * （从连接池中清除掉的含义是把它所对应的连接池的entry置为无效，并且关掉对应的connection，shutdown对应socket的输入和输出流。这个方法的调用时间是需要设置的）。
         * 结论如下：
         * 关闭stream和response的区别在于前者会尝试保持底层的连接alive，而后者会直接shut down并且丢弃connection。
         * socket是和ip以及port绑定的，但是host相同的请求会尽量复用连接池里已经存在的connection
         * （因为在连接池里会另外维护一个route的子连接池，这个子连接池中每个connection的状态有三种：leased、available和pending，
         * 只有available状态的connection才能被使用，而fully consume entity就可以让该连接变为available状态），如果host地址一样，则优先使用该connection。
         */
        var closeableHttpResponse= httpclient.execute(httpPost);
        var value =org.apache.http.util.EntityUtils.toString(closeableHttpResponse.getEntity());
        System.out.println("响应结果:"+value);

        /**
         * 关闭HttpClient
         * 调用httpClient.close()会先shut down connection manager，然后再释放该HttpClient所占用的所有资源，关闭所有在使用或者空闲的connection包括底层socket。
         * 由于这里把它所使用的connection manager关闭了，所以在下次还要进行http请求的时候，要重新new一个connection manager来build一个HttpClient
         * （也就是在需要关闭和新建Client的情况下，connection manager不能是单例的）。
         */
        httpclient.close();

        /**
         * 长连接，关于keep-alive
         * Http1.1默认进行的长连接
         * 在HttpClient.execute得到response之后的相关代码中，它会先取出response的keep-alive头来设置connection是否resuable以及存活的时间。
         * 如果服务器返回的响应中包含了Connection:Keep-Alive（默认有的），但没有包含Keep-Alive时长的头消息，HttpClient认为这个连接可以永远保持。
         * 不过，很多服务器都会在不通知客户端的情况下，关闭一定时间内不活动的连接，来节省服务器资源。
         * 在这种情况下默认的策略显得太乐观，我们可能需要自定义连接存活策略，也就是在创建HttpClient的实例的时候用下面的代码。（xxx为自己写的保活策略）
         * ClosableHttpClient client =HttpClients.custom().setKeepAliveStrategy(xxx).build();
         * 不适用的场景：
         * 如果使用默认的长连接就会一直只去请求对方的某一台服务器，不管怎么说，虽然调用的确实是相同host的主机对功能来说是没有问题的，但万一对方服务器被这样弄挂了呢？
         * 并且这种情况下要是使用了dns负载均衡技术，那么dns的负载均衡将不能被执行到！这显然不是我们所希望的。
         * 只要是长连接的connection，在代码中调用各种close或者release方法都不能把connection真正关掉，除非把整个httpClient.close
         */

        /**
         * 短连接，
         * 如果应用场景下需要的是短连接，这样只要在request中添加Connection:close的头部，就可以保证这个链接在这次请求完成之后就被关掉，只用一次。
         * 同时发现，如果头中既有Connection:Keep-Alive又有Connection:close的话，Connection:close并不会有更高的优先级，依旧会保持长连
         */

        /**
         * 连接池管理
         * 前面也有说到关于从连接池中取可用连接的部分逻辑。完整的逻辑是：
         * 在每收到一个route请求后，连接池都会建立一个以这个route为key的子连接池，当有一个新的连接请求到来的时候，它会优先匹配已经存在的子连接池们，
         * 如果之前已经有过以这个route为key的子连接池，那么就会去试图取这个子连接池中状态为available的连接，如果此时有可用的连接，则将取得的available连接状态改为leased的，取连接成功。
         * 如果此时子连接池没有可用连接，那再看是否达到了所设置的最大连接数和每个route所允许的最大连接数的上限，
         *   如果还有余量则new一个新的连接，或者取得lastUsedConnection，关闭这个连接、把连接从原来所在的子连接池删除，再lease取连接成功。
         *   如果此时的情况不允许再new一个新的连接，就把这个请求连接的请求放入一个queue中排队等待，直到得到一个连接或者超时才会从queue中删去。一个连接被release之后，会从等待连接的queue中唤醒等待连接的服务进行处理。
         */

        /**
         * 当连接被管理器收回后，这个连接仍然存活，但是却无法监控socket的状态，也无法对I/O事件做出反馈。
         * 如果连接被服务器端关闭了，客户端监测不到连接的状态变化（也就无法根据连接状态的变化，关闭本地的socket）。
         * HttpClient为了缓解这一问题造成的影响，会在使用某个连接前，监测这个连接是否已经过时，如果服务器端关闭了连接，那么连接就会失效
         */

        /**
         * 结论
         * 连接池最大连接数，不配置为20
         * 同个route的最大连接数，不配置为2
         * 去连接池中取连接的超时时间，不配置则无限期等待
         * 与目标服务器建立连接的超时时间，不配置则无限期等待
         * 去目标服务器取数据的超时时间，不配置则无限期等待
         * 要fully consumed entity，才能正确释放底层资源
         * 同个host但ip有多个的情况，请谨慎使用单例的HttpClient和连接池
         * HTTP1.1默认支持的是长连接，如果想使用短连接，要在request上加Connection:close的header，不然长连接是不可能自动被关掉的！
         * 一定要结合实际情况来看是否需要设置，不然可能导致严重的问题。
         */



    }
}
