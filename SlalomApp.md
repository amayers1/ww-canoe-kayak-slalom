# Whitewater Canoe Kayak Slalom Application #

This application is designed to simplify the Race Organizers and Volunteers job of running a race.  It has simple features for use in Division 3/4 racing, and more advanced features for Division 2 through Premier Division racing.

## Releases ##

Currently BETA release code is available here:

[Downloads](https://drive.google.com/folderview?id=0B0uX48lguh9aSmpVbnlNbDF1d1E&usp=sharing)


## Requirements ##
  * PC - Windows XP or better,  Mac - OS X 10.5 or better,  or any Linux
  * Java 1.6 JVM or better

To check if Java is already installed, go to a command line and type 'java -version'.  If you get a response and the version is at least 1.6, then you meet the java requirement.  Otherwise you can download java from Oracle here [Java version 6 Java Runtime Environment](http://www.oracle.com/technetwork/java/javase/downloads/jre6-downloads-1637595.html)

## Installation ##
  1. Click on the download link under [Releases](SlalomApp#Releases.md) at the top of the page
  1. Click on the link that says Downloads for Slalom App
  1. Right click on the _Current Stable Release_ link and select Download from the properties menu
  1. Download the SlalomApp.jar and all .cmd (Windows) or .command (Mac/Linux) files
  1. You may move and rename the folder where ever you like
  1. Installation is complete

## Running the Applications ##
There is a _Demo_ mode that runs a simulated race, this may be the easiest way to see how the system works.   Under Windows run the RaceSimulation.cmd file and under Linux or Mac OS X run RaceSimulation.command.  The _Demo_ race will runs itself with a variety of windows open, and you will still be able to use the controls manually.  You make interact and use any of the controls while the demo mode race is running.

There are 2 applications, SlalomApp.jar contains both programs, the main application which handles all configuration, timing, etc., and the remote **SectionJudge** application  which handles scoring of penalties.  **SectionJudge** functions can be done from **SlalomApp** for lower level races or where section judges do not have suitable hardware to run the **SectionJudge** application.

To run the main **SlalomApp** application simply double click on SlalomApp.jar or double click  SlalomApp.command (Mac OS X or Linux).  If this doesn't work in your environment try the command line:

`java -jar SlalomApp.jar`

To run a section judging stand alone application, use the SectionJudge.command (Mac OS X or Linux) or cut and paste this command line.

`java -cp SlalomApp.jar com.tcay.slalom.UI.client.SectionJudgeClient`

## Support ##

We will do our best to support you at:
[Support You](https://groups.google.com/forum/#!forum/ww-canoe-kayak-slalom-discuss)

You can help by supporting us here:
[Support US](http://tcay.com/SlalomApp/PayPalDonationForSlalomApp.html)


## Features ##
  * Simple User interface - Dynamic and Context Sensitive
  * Integration of electronic timing equipment - Tag Heuer CP520/540
  * Realtime results
  * Automatic configuration for timing equipment hardware interface
  * Output to spectator / competitor scoreboard
  * Manual backup or primary timing for up to 3 racers on course simultaneously
  * Start list import
  * Selectable reruns
  * Internationalization - National Language Support
  * Section Judging input from multiple remote stations or a single station
  * Configuration of all race parameters
    1. Judging Sections
    1. Indication of upstream gates
    1. ICF Penalty diagrams and codes




## Sample Screen Shots ##

---

### Race Configuration ###

![http://tcay.com/SlalomApp/SlalomAppConfigurationScreen.png](http://tcay.com/SlalomApp/SlalomAppConfigurationScreen.png)

The **Race Configuration** screen has typical information about a race, Name, Date, Location and total number of gates. It also has course configuration information about sections which is used to allocate gates to a section so that **Section Judges** are able to score only the gates assigned to them.   **Upstream Gates** are selected in order to give the penalty result screens UI proper gate icons.

**Advanced Options** are those used either for demo purposes or for top tier races.  They are  described with _tool tips_ popups when the user _hovers_ over the control.


_Screen shot Race Configuration Screen_



---


### Timing ###

![http://tcay.com/SlalomApp/SlalomAppTimingScreen.png](http://tcay.com/SlalomApp/SlalomAppTimingScreen.png)

_Screen shot from Timing Screen_


The timing screen is divided into 3 sections plus a status bar at the bottom:
  * **Start List** - which has a picklist of all the entries that have not yet started the run.  The first boat on the  start list that has not yet started is automatically _promoted_ to the Starting Block.  This can be overridden by choosing from the **Pick List Combo Box** and pressing the **Select Racer** button

  * **Starting Block** has only 2 controls, **Start** button that starts the clock for the racer leaving the **Starting Block** and the **DNS** button indicating the racer Did Not Start (will not start this run).

  * **Finish Line** handles 3 racers simultaneously on the course and has 2 controls for each boat that has not yet crossed the finish, **Finish** which stops the clock for that associated boat, and **DNF** indicating the boat Did Not Finish. These buttons do not show for a boat once they have finished or DNF.ed.

  * **Status Bar** shows :
    * IP address of the computer/device
    * Which run number is currently taking place
    * Indicators for **Section Judging** stations that are currently communicating via the network
    * An indicator for electronic timing equipment status (Tag Heuer CP 5xx series is currently supported)


---


![http://tcay.com/SlalomApp/SlalomAppTimingScreenPickList.png](http://tcay.com/SlalomApp/SlalomAppTimingScreenPickList.png)

_Screen shot from Timing Screen - Pick List for Entry to be placed in start block_

Normally the Starting block is automatically propagated from the race's start list once a racer in the starting block starts.  This picklist allows for starting racers out of order without DNSing them.  This is useful in Division 3 and 4 races where it is not critical if racers start in order.


---


### Gate Judging ###


![http://tcay.com/SlalomApp/SlalomAppSectionJudging.png](http://tcay.com/SlalomApp/SlalomAppSectionJudging.png)

_Screen shot from Section Judging Screen_

**Section Judge** screen is designed to be used on-station at the section, but can also be used from a central **Race Control** station.  In the latter case each section can have a window to allow selecting of penalties as they are called in (sometime out of sequence), or all sections can be scored at once in a single window. The former method makes it easier to track when all gates have scored for a boat.

With the Advanced Option for **Use ICF Penalties** is enabled, penalties will be marked with appropriate codes for 50s and diagrams for touches **TBD - WIP**.  See the Letter in parenthesis next to the 50.  This is selected from [\*ICF Missed Gate Codes\*](SlalomApp#ICF_Missed_Gate_Codes.md).





---


![http://tcay.com/SlalomApp/SlalomAppSectionJudgingPickList.png](http://tcay.com/SlalomApp/SlalomAppSectionJudgingPickList.png)

_Screen shot from Section Judging Screen Boat Selection Pick List_


The picklist shows Boats that have already been scored from the section with a red background, and those that remain to be scored with a green background.  The boat selection list will default to the first boat in the section that has not been scored.


---


### ICF Missed Gate Codes ###

![http://tcay.com/SlalomApp/SlalomAppICFMissedGateCodes.png](http://tcay.com/SlalomApp/SlalomAppICFMissedGateCodes.png)

_Screen shot from Section Judging ICF Missed Gate codes_



---


### Demo Mode ###

![http://tcay.com/SlalomApp/SlalomAppDemoRaceDesktop.png](http://tcay.com/SlalomApp/SlalomAppDemoRaceDesktop.png)

_A Demo Mode Race with all windows running on a laptop_



---


![http://tcay.com/SlalomApp/SlalomAppOnXPunderOSX.png](http://tcay.com/SlalomApp/SlalomAppOnXPunderOSX.png)

_A Demo Mode Race running under MS Windows (in VirtualBox on MacBook Air)_


---




### Internationalization ###

![http://tcay.com/SlalomApp/SlalomAppConfigurationi18n.png](http://tcay.com/SlalomApp/SlalomAppConfigurationi18n.png)




<a href='Hidden comment: 

= Introduction =

Add your content here.


= Details =

Add your content here.  Format your content with:
* Text in *bold* or _italic_
* Headings, paragraphs, and lists
* Automatic links to other wiki pages

'></a>