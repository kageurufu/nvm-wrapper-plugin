# [Jenkins pyenv Plugin (pyenv-wrapper)](https://wiki.jenkins-ci.org/display/JENKINS/pyenv+Wrapper+Plugin)

[![Build Status](https://img.shields.io/travis/kageurufu/pyenv-wrapper-plugin/master.svg?style=flat)](https://travis-ci.org/kageurufu/pyenv-wrapper-plugin)

A java/groovy based replacement for the existing pyenv plugins, removing the need
 for special `pysh` directives and allowing simpler integration into your jobs.

## Usage
- Please follow this [steps](https://wiki.jenkins-ci.org/display/JENKINS/Pyenv+Wrapper+Plugin)

## Build
- It was built using gradle 2.3 and Java 1.8

- 'gradle jpi' - Build the Jenkins plugin file, which can then be
  found in the build directory. The file will currently end in ".hpi".
- 'gradle install' - Build the Jenkins plugin and install it into your
  local Maven repository.
- 'gradle uploadArchives' (or 'gradle deploy') - Deploy your plugin to
  the Jenkins Maven repository to be included in the Update Center.
- 'gradle server' - Run a local jenkins to test

## Features

- Installs `pyenv`
- Installs python version configured for job.
- Amends build environment to use configured python version.

## Acknowledgements

Based on :

[Jenkins rvm plugin](https://github.com/jenkinsci/rvm-plugin),
[Jenkins pyenv plugin](https://github.com/codevise/jenkins-pyenv-plugin), and
[Jenkins nvm wrapper plugin](https://github.com/jenkinsci/nvm-wrapper-plugin)

## License

Copyright (c) 2017 Franklyn Tackitt. This software is licensed under the MIT License.

Derived from code Copyright (c) 2017 Tomas Salazar, also licensed under the MIT License.

Please fork and improve.
