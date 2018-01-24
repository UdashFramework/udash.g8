# Packager

This module keeps configuration of deployment packages build. It can also override default configurations
from the `backend` module. In `src/main/resources` you can find customized `application.conf` which will override
the `application.conf` file from the `backend` module.

## How to create a package?

You can create pretty `zip` package with your application using the following command: 
`sbt packager/universal:packageBin`. Your package will be created in the `packager/target/universal` directory.
Call a starting script from the `bin` directory to start the application.

## What's next?

Take a look at the [SBT Native Packager](https://github.com/sbt/sbt-native-packager) 
docs for more details. You can find there a lot of possible configurations for building 
different types of packages, e.g., `.zip`, `.deb` or `.msi`.

