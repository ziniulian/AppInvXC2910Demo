@echo off
xcopy L:\Doc\SVN\Work\OtherAS\XC2910Demo\app\src\main L:\Doc\Git\AppInvXC2910Demo\app\src\main\ /S
xcopy L:\Doc\SVN\Work\OtherAS\XC2910Demo\app\libs L:\Doc\Git\AppInvXC2910Demo\app\libs\ /S
copy L:\Doc\SVN\Work\OtherAS\XC2910Demo\app\build.gradle L:\Doc\Git\AppInvXC2910Demo\app
pause
