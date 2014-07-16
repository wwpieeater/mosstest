mosstest
========

Open-source Minetest clone. Refer to wiki for details.

Pull requesters:  
Don't touch the thread pool or the scripting interface. 
Electrocution, drowning, or security bots attacking you may occur.

pom.xml refers to non-maven jars in a custom jMonkey repository. `pom.xml` refers to that repository, although it may go down when the forum or wiki goes down for updates or maintenance.

## Licensing and legalities

The entire project is licensed under the GNU GPLv3 or higher. 

The burden of ensuring license compatibility for scripts is delegated to the script author. Server operators and players are not responsible 
for non-compliant scripts. As obfuscated, "JSfuck"ed, minified, or otherwise non-original script files are not "the preferred way of making modifications" 
they may not be used as scripts without permission from the authors. As a courtesy, please comment scripts, although comments are not required.

## Privacy laws

The authors of a script, or the Mosstest developers will not be responsible for violations of privacy laws in any jurisdictions. The burden of ensuring privacy laws are followed is delegated to server owners (as a courtesy, script authors should explicitly state what data is collected by their scripts)

The Mosstest engine itself receives, and may *collect in any format, including the database or logfiles*, player usernames, IP addresses, and passwords.

In addition, operating system and network details may be collected as an artifact of network communication, for example where OS-specific or network-equipment-specific "quirks" of how network connections are created and negotiated, or details such as text encodings or line ending characters are recorded in these logs. By running this software, the user acknowledges the recording and potential use of these details, by any game server.

## Security

While Mosstest strives to be secure, the script sandbox on a client will execute code given by a server. In case of a security breach, a rogue server can compromise a client. Please play on untrusted servers at your own risk.

If you believe a security bug exists, please report it at [Launchpad](https://bugs.launchpad.net/mosstest/+filebug). Before submitting, please mark it as private security. Developers will make it public as soon as reasonably possible.
