# CIS4515_RSAEncryption

## About This Lab
This lab has two goals - getting you familiar with Content Providers, and letting you play around
with RSA encryption.  The point of a Content Provider is to provide a common access point for one or
more apps who want to interact with some shared data resource.  While the lab itself is relatively
simple, learning the process of setting up a Content Provider will help you greatly in the future.

## About My Solution
__ NOTE: Please DO NOT consider my solution to be the ONLY solution.  There are as many right
solutions as there are people to think of them.  This is just an example of how this problem MAY be
solved. __

I chose to place my content provider in a separate library module so that future applications that
wanted to use the provider could access a common resource, rather than depending on an explicit
implementation in the RSAEncryption application.  In practice, however, because this lab is so simple,
implementing the provider in the same app module as the encryption logic is just fine.

## Helpful Resources

### General Information
* https://developer.android.com/reference/android/content/ContentProvider.html
* https://developer.android.com/guide/topics/providers/content-provider-basics.html

### Tutorials
* https://www.tutorialspoint.com/android/android_content_providers.htm
* https://www.grokkingandroid.com/android-tutorial-writing-your-own-content-provider/
* https://developer.android.com/guide/topics/providers/content-provider-creating.html
