cd ..
mkdir -p translate/servercore
mkdir -p translate/scripting
mkdir -p translate/launcher
msgcat -P src/net/mosstest/servercore/messages.properties -o translate/servercore/servercore.pot
msgcat -P src/net/mosstest/scripting/messages.properties -o translate/scripting/scripting.pot
msgcat -P src/net/mosstest/launcher/messages.properties -o translate/launcher/launcher.pot
