# Claptrap
Goodbye nonsensical email testing.

Claptrap is a simple commandline utility that creates a fake smtp server with a web frontend
to be used when testing the email functionality of your application.

#Usage

##Running Claptrap
Getting started with Claptrap is easy. Simply download the latest jar from the releases page, place it somewhere where you can easily access it and run it with from a console with java -jar claptrap.jar.

Claptrap also allows various flags to change various settings. For easy reference these can be found by running java -jar claptrap.jar -h.

`-b,--box-size <email-count>   sets max number of emails claptrap holds for each server (default: 500)`

`-d,--debug                    enable debugging information`

`-h,--help                     show this help message`

`-p,--http-port <port>         sets the http port for the web server`

`-s,--smtp-port <port>         sets the port for the smtp server`

`-v,--version                  displays the version of claptrap installed`

##Using Claptrap
To use Claptrap, simply point your browser to the host Claptrap is running on and the port that is recorded in the console. From there you are free to select a server from the dropdown list and view emails.

#License
Claptrap is licensed under the very permissive MIT License. I wanted a to make a neat tool to help me at work and decided why not share it with the world. Hope you enjoy using it as much as I enjoyed building it!

