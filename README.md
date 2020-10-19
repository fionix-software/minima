![Native CI](https://github.com/fionix-software/minima/workflows/Native%20CI/badge.svg)

# Minima

Minima is a minimal cross platform iCress application. Minima name is picked to be an alternative name for Lumos. In Harry Potter series, there is a spell called 'Lumos Maxima' which is the incantation to a charm that can be used to produce a blinding flash of bright white light from the tip of the wand. As an alternative, the Minima is the opposite for Maxima. Why picked the spell? Well, there is an existing iCress application at early time called 'Lumos' which was discontinued.

Minima will be migrated to Flutter starts from version 4. Its initial development is on 5 Mar 2017 and was first published on 6 Mar 2017. Minima migration will start its development soon.

This application uses Flutter framework and Dart programming language to ensure compatibility across multiple platform. Flutter is Google’s UI toolkit for building beautiful, natively compiled applications for mobile, web, and desktop from a single codebase.

What is open source and why? Open-source software (OSS) is a type of computer software in which source code is released under a license in which the copyright holder grants users the rights to study, change, and distribute the software to anyone and for any purpose. Open-source software embrace strong values of community, collaboration, and transparency, for the mutual benefit of the platform and its users. Other than that, it is more cost-effective, flexible, and secure.

Officialy available on [Google Play Store](https://play.google.com/store/apps/details?id=net.fionix.minima).

# Limitation

- Unable to support homescreen app widget and notification widget (Non-cross platform feature)
- Unable to use auto-fetch method as the application was given a warning regarding security from UiTM staff
- Legacy version will only available as Github release

# Installation

## Flutter

1. Make sure to set up your working environment according to the Flutter installation guide as mentioned [here](https://flutter.dev/docs/get-started/install).
2. Make sure to set up your working environment with SDK for your target platform, as the name suggests Android SDK, iOS SDK etc.
3. Use the following command to compile:

```bash
# run flutter in debug mode
flutter run

# run flutter as release
flutter run --release
```

## Android Studio (Legacy)

1. Make sure to set up your working environment according to Android Studio installation guide as mentioned [here](https://developer.android.com/studio/install).
2. Use Android Studio's IDE 'Run' button to compile and run.

# Support

If there is any issue arise, please use the [issue tracker](https://github.com/fionix-software/minima/issues) wisely. Please make sure there is no duplication and try your best to explain in detail and respectful manner.

Please state your issue as shown below:
- **Title**: Short brief what doesn't work / issue.
- **Description**: Detail on what is the issue.
- **Steps to Reproduce**: Clearly and chronologically.
- **Expectation**: How would you think it should behave / work.
- **Platform Info**: Please provide your platform (Android / Linux desktop / etc.) and platform model along with its brand (Apple iPhone 11 / Google Pixel 4 / etc.).

Currently Fionix only consist of one person. You can find him on twitter, [@NazebZurati](https://twitter.com/NazebZurati). If there are any concerns, please let him know.

# Convention and branching

Flutter already have implemented effective code style checker, which means less human job in standardizing the code style. For now, the only exceptions are stated as below:
- Disable dartfmt's line length by set it to value 0

However, implementation of design patterns and best practices in the source code is welcome but make sure it do more benefit than do harm.

Branching format is **type**/**change-title**. Type is only applicable to feature and fix. Pull request is considered a feature branch. Make sure your title consists of 2 to 5 word sentence. The title should be clear and meaningful. Use '-' to substitute the space between word, and not '\_'. Branch name should be singular for both type and change title.

# Design philosophy

Fionix is a brand of a collection of open source software and game title. One of the feature that required a brand to keep its consistency in its project with the brand owns design philosophy. Make sure to check with Fionix Design Philosophy (will be made public soon). You may provide edge cases, suggestions and report if something doesn't work out for the user-end perspective / user experience.

# Contribution

You can contribute in many ways. You don't have to contribute code and you can contribute by planning evens, design, write documentations, reviews, promotes and many more! Refer [here](https://opensource.guide/how-to-contribute/) for more. You can direct message the maintainer on twitter (as stated above) for inquiries.

If you were to contribute to the project by code, please follow Github's Fork & Pull guide stated [here](https://reflectoring.io/github-fork-and-pull/). Make sure to update your forked repository to upstream before working on it, or at least before submitting the changes. After submitting the changes, please make a pull request and have a discussion. Make sure to follow the branching model.

# License

This software source code is licensed under GNU General Public License (GPL) v3.0. 

You cannot use any Fionix's project and brand identity (including project name, project icon / logo, Fionix logo etc.) and please do respect the decision.
