
REM position yourself in the class directory !

cd classes

C:\Programmi\Java\jdk1.6.0_05\bin\javah -o ..\..\win32\jcomport\jcomport.h -verbose -classpath . com.engidea.win32jcom.WinjcomPort com.engidea.win32jcom.WinjcomIdentifier com.engidea.comm.SerialPort

cd ..

pause
