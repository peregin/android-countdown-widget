# Countdown Days Widget For Android

![logo](https://raw.github.com/peregin/android-countdown-widget/master/doc/100_icon.jpg "Countdown Days")

The widget will help you to countdown the days until a given date.
It shows the remaining days or even the working days left if you are interested in such a counter.

## Project Setup

Some notes regarding the project setup and structure:
* The project was extracted to be published on GitHub from a multi module structure, therefore the testing module is missing from this migrated version. However only the tests are missing, the code is fully functional and it can be deployed.
* The project is configured with maven. It can be compiled and packaged easily via command line or you can use one of your preferred IDE.
* maven-android-plugin needs the *ANDROID_HOME* environment variable pointing to the current Android SDK installation.
* I was using Eclipse and IntelliJ as IDE to develop this widget, the meta files generated by these IDEs are committed as well

## Work In Progress And Wish List

I started with a simple version of the widget, then over the time the enhancements were added.
I also got a lot of feedback, ideas and comments, which motivated me to continue the development.
Here are the most popular requests:
* multi-size widget - work in progress - currently only 1x1 size is available
* show remaining weeks or months (not days only)
* consider adding holidays in the calculation of the remaining days
* be able to associate some customizable icons to specific events

## The Beginnings

This is how the widget started (the story telling part;), I couldn't find a simple widget which counts the working days as well, so I decided to write one :)
At the very beginning I just sketched how the widget would look like:

![start](https://raw.github.com/peregin/android-countdown-widget/master/doc/start.jpg "Sketch")

Then the next step was to prepare the vector based graphics with Inkscape:

![graphics](https://raw.github.com/peregin/android-countdown-widget/master/doc/finish.jpg "Graphics")

## About

For more details please visit the widget from the [Google Play](http://play.google.com/store/apps/details?id=peregin.android.countdown)

Feel free to contact me if you have any questions.




