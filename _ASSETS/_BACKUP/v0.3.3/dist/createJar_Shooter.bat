
: change to eclipse output
  cd ..\bin
  
: package jar  
  jar cvMf Shooter_unsigned.jar de/ res/ META-INF/

: change back to dist dir and copy packaged jar
  cd ..\dist
  move ..\bin\Shooter_unsigned.jar Shooter_unsigned.jar

: sign
  jarsigner -keystore E:\eclipse_workspace\Shooter\trunk\dist\keystore.jks -storepass chrisy -keypass chrisy -signedjar Shooter.jar Shooter_unsigned.jar Shooter

: delete unsigned
  del Shooter_unsigned.jar
  
: paws
  pause


: HOW TO create a keystore before signing
: keytool -genkey -alias Shooter -keyalg RSA -keystore E:\eclipse_workspace\Shooter\trunk\dist\keystore.jks -keysize 2048 -validity 100000
