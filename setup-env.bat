@echo off
echo Setting up Twilio Environment Variables for SIH Application
echo.
echo Please enter your Twilio credentials:
echo.

set /p TWILIO_ACCOUNT_SID="Enter your Twilio Account SID: "
set /p TWILIO_AUTH_TOKEN="Enter your Twilio Auth Token: "
set /p TWILIO_TRIAL_NUMBER="Enter your Twilio Trial Number (e.g., +12272380745): "

echo.
echo Setting environment variables...
setx TWILIO_ACCOUNT_SID "%TWILIO_ACCOUNT_SID%"
setx TWILIO_AUTH_TOKEN "%TWILIO_AUTH_TOKEN%"
setx TWILIO_TRIAL_NUMBER "%TWILIO_TRIAL_NUMBER%"

echo.
echo Environment variables set successfully!
echo Please restart your IDE/command prompt for changes to take effect.
echo.
pause
