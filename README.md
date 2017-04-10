stomp-client
============
[![MIT](https://img.shields.io/badge/licence-MIT-lightgrey.svg?style=flat)](https://tldrlegal.com/license/mit-license) [![Build Status](https://travis-ci.org/ccamel/stomp-client.svg?branch=master)](https://travis-ci.org/ccamel/stomp-client) [![Coverage Status](https://coveralls.io/repos/github/ccamel/stomp-client/badge.svg?branch=master)](https://coveralls.io/github/ccamel/stomp-client?branch=master)

> [Scala] implementation of a [STOMP] 1.2 client library for both the backend and frontend.

## Description

`stomp-client` is a small library that provides a full [STOMP 1.2] client implementation for the [Scala] programming language, and more widely the JVM platform. 

This enables the communication with any [STOMP] compliant messaging brokers and servers (like [RabbitMQ](https://www.rabbitmq.com/), [ActiveMQ Appolo](http://activemq.apache.org/apollo/)).

It can be used:

- on the backend side, on a JVM with a language of choice (java, scala, clojure...)
- on the frontend side (Web Browser, using WebSockets ), using [Scala.js].

Library is designed to play well with other frameworks, like [akka](http://akka.io/) framework.

## Project Status

:warning: This is a work-in progress.

### Todo

- [X] project initialization
- [X] data model for [STOMP frames](https://stomp.github.io/stomp-specification-1.2.html#STOMP_Frames)
- [ ] unit tests
- [ ] codec for STOMP frames (serialization/deserialization)
- [ ] implementation of the protocol (session, transaction)
- [ ] write user documentation (usage, configuration, examples)
- [ ] write minimal technical documentations (implementation details)

## License

[MIT] © [Chris Camel]

[Scala]: https://www.scala-lang.org/
[STOMP]: https://stomp.github.io/
[STOMP 1.2]: https://stomp.github.io/stomp-specification-1.2.html
[Scala.js]: https://www.scala-js.org/
[Chris Camel]: https://github.com/ccamel
[MIT]: https://tldrlegal.com/license/mit-license
