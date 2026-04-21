@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
set "PROPS_FILE=%SCRIPT_DIR%.mvn\wrapper.properties"

if not exist "%PROPS_FILE%" (
  echo Missing wrapper properties: "%PROPS_FILE%"
  exit /b 1
)

for /f "tokens=1,* delims==" %%A in (%PROPS_FILE%) do (
  if /I "%%A"=="maven.version" set "MAVEN_VERSION=%%B"
  if /I "%%A"=="maven.baseUrl" set "MAVEN_BASE_URL=%%B"
)

if not defined MAVEN_VERSION (
  echo Missing maven.version in "%PROPS_FILE%"
  exit /b 1
)

if not defined MAVEN_BASE_URL (
  echo Missing maven.baseUrl in "%PROPS_FILE%"
  exit /b 1
)

set "MAVEN_DIR=%SCRIPT_DIR%.mvn\apache-maven-%MAVEN_VERSION%"
set "MAVEN_ZIP=%SCRIPT_DIR%.mvn\apache-maven.zip"
set "MAVEN_URL=%MAVEN_BASE_URL%/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip"

if not exist "%MAVEN_DIR%\bin\mvn.cmd" (
  echo Downloading Apache Maven %MAVEN_VERSION%...
  powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "$ProgressPreference='SilentlyContinue';" ^
    "New-Item -ItemType Directory -Force '%SCRIPT_DIR%.mvn' | Out-Null;" ^
    "Invoke-WebRequest -UseBasicParsing '%MAVEN_URL%' -OutFile '%MAVEN_ZIP%';" ^
    "Expand-Archive -LiteralPath '%MAVEN_ZIP%' -DestinationPath '%SCRIPT_DIR%.mvn' -Force"
  if errorlevel 1 exit /b 1
)

call "%MAVEN_DIR%\bin\mvn.cmd" %*
exit /b %ERRORLEVEL%
