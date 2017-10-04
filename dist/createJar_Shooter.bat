
: change to eclipse output
  cd ..\bin

: package jar
  "C:\Program Files\Java\jdk1.8.0_144\bin\jar" cvMf Shooter_unsigned.jar de/ res/ META-INF/

: change back to dist dir and copy packaged jar
  cd ..\dist
  move ..\bin\Shooter_unsigned.jar Shooter_unsigned.jar

: sign
"C:\Program Files\Java\jdk1.8.0_144\bin\jarsigner" -keystore c:\users\stock\workspaces\java\Shooter\dist\keystore.jks -storepass chrisy -keypass chrisy -signedjar Shooter.jar Shooter_unsigned.jar Shooter

: delete unsigned
  del Shooter_unsigned.jar

: paws
  pause


: HOW TO create a keystore before signing
: "C:\Program Files\Java\jdk1.8.0_144\bin\keytool" -genkey -alias Shooter -keyalg RSA -keystore c:\users\stock\workspaces\java\Shooter\dist\keystore.jks -keysize 2048 -validity 100000
