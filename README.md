stomp-client
============
[![MIT](https://img.shields.io/badge/licence-MIT-lightgrey.svg?style=flat)](https://tldrlegal.com/license/mit-license)

> [Scala](https://www.scala-lang.org/) implementation of a [STOMP] 1.2 client library for both the backend and frontend.

## Description

`stomp-client` is a small library for communicating with [STOMP] compliant messaging brokers and servers (like [RabbitMQ](https://www.rabbitmq.com/), [ActiveMQ Appolo](http://activemq.apache.org/apollo/))

It provides a full [STOMP 1.2] client that can be used:
- on the backend side, on a JVM with a language of choice (java, scala, clojure...)
- on the frontend side (Web Browser, using WebSockets ), using [Scala.js].

Library has been designed to play well with other frameworks, like [akka](http://akka.io/) framework.

## Project Status

This is work-in progress. Any helps welcome !

## License

[MIT] © [Chris Camel]


[STOMP]: https://stomp.github.io/
[STOMP 1.2]: https://stomp.github.io/stomp-specification-1.2.html
[Scala.js]: https://www.scala-js.org/