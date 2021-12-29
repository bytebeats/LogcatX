# LogcatX

[![GitHub latest commit](https://badgen.net/github/last-commit/bytebeats/LogcatX)](https://github.com/bytebeats/LogcatX/commit/)
[![GitHub contributors](https://img.shields.io/github/contributors/bytebeats/LogcatX.svg)](https://github.com/bytebeats/LogcatX/graphs/contributors/)
[![GitHub issues](https://img.shields.io/github/issues/bytebeats/LogcatX.svg)](https://github.com/bytebeats/LogcatX/issues/)
[![Open Source? Yes!](https://badgen.net/badge/Open%20Source%20%3F/Yes%21/blue?icon=github)](https://github.com/bytebeats/LogcatX/)
[![GitHub forks](https://img.shields.io/github/forks/bytebeats/LogcatX.svg?style=social&label=Fork&maxAge=2592000)](https://github.com/bytebeats/LogcatX/network/)
[![GitHub stars](https://img.shields.io/github/stars/bytebeats/LogcatX.svg?style=social&label=Star&maxAge=2592000)](https://github.com/bytebeats/LogcatX/stargazers/)
[![GitHub watchers](https://img.shields.io/github/watchers/bytebeats/LogcatX.svg?style=social&label=Watch&maxAge=2592000)](https://github.com/bytebeats/LogcatX/watchers/)

Capture, Display, Save, Share Android logs in Android devices.

## Features

* Floating window. Shown overlay Activities's Window.
* Dark theme. Supported and followed system's Light Theme or Dark Theme.

## Graph Effects

<img src="/arts/logcatx_toolbar.gif" width="360" height="750"/>
<img src="/arts/logcatx_options.gif" width="360" height="750"/>
<img src="/arts/logcatx_quit.gif" width="360" height="750"/>

## How to use?

In your module `build.gradle` , add dependency like this:
```
dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    
    ......

    debugImplementation project(':logcatx')
}
```

Then, `LogcatX` won't be compiled into your apk when releasing.

## Stargazers over time
[![Stargazers over time](https://starchart.cc/bytebeats/LogcatX.svg)](https://starchart.cc/bytebeats/LogcatX)

## Github Stars Sparklines
[![Sparkline](https://stars.medv.io/bytebeats/LogcatX.svg)](https://stars.medv.io/bytebeats/LogcatX)

## Contributors
[![Contributors over time](https://contributor-graph-api.apiseven.com/contributors-svg?chart=contributorOverTime&repo=bytebeats/LogcatX)](https://www.apiseven.com/en/contributor-graph?chart=contributorOverTime&repo=bytebeats/LogcatX)

## MIT License

    Copyright (c) 2021 Chen Pan

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
