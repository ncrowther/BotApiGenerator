# ######################################################
# RPA API Test Script
#
# Author: Nigel T. Crowther
# Copyright: IBM 2022
# ######################################################


## Disable SSL Checks
 add-type @"
    using System.Net;
    using System.Security.Cryptography.X509Certificates;
    public class TrustAllCertsPolicy : ICertificatePolicy {
        public bool CheckValidationResult(
            ServicePoint srvPoint, X509Certificate certificate,
            WebRequest request, int certificateProblem) {
            return true;
        }
    }
"@

Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName System.Drawing

function DisableSSL {
   [System.Net.ServicePointManager]::CertificatePolicy = New-Object TrustAllCertsPolicy
}

$Global:defaultUserGbl = 'admin@ibmdba.com'
$Global:defaultPasswordGbl = 'passw0rd'

#$Global:defaultUserGbl = 'ncrowther@uk.ibm.com'
#$Global:defaultPasswordGbl = '*****'

# ######################################################
# Get the RPA host.  For on prem this is localhost:30000
# ######################################################
function GetHost {
    $form = New-Object System.Windows.Forms.Form
    $form.Text = 'Host Selection'
    $form.Size = New-Object System.Drawing.Size(300,200)
    $form.StartPosition = 'CenterScreen'

    $OKButton = New-Object System.Windows.Forms.Button
    $OKButton.Location = New-Object System.Drawing.Point(75,120)
    $OKButton.Size = New-Object System.Drawing.Size(75,23)
    $OKButton.Text = 'OK'
    $OKButton.DialogResult = [System.Windows.Forms.DialogResult]::OK
    $form.AcceptButton = $OKButton
    $form.Controls.Add($OKButton)

    $CancelButton = New-Object System.Windows.Forms.Button
    $CancelButton.Location = New-Object System.Drawing.Point(150,120)
    $CancelButton.Size = New-Object System.Drawing.Size(75,23)
    $CancelButton.Text = 'Cancel'
    $CancelButton.DialogResult = [System.Windows.Forms.DialogResult]::Cancel
    $form.CancelButton = $CancelButton
    $form.Controls.Add($CancelButton)

    $label = New-Object System.Windows.Forms.Label
    $label.Location = New-Object System.Drawing.Point(10,20)
    $label.Size = New-Object System.Drawing.Size(280,20)
    $label.Text = 'Please make a selection from the list below:'
    $form.Controls.Add($label)

    $listBox = New-Object System.Windows.Forms.Listbox
    $listBox.Location = New-Object System.Drawing.Point(10,40)
    $listBox.Size = New-Object System.Drawing.Size(260,20)

    $listBox.SelectionMode = 'MultiExtended'

    [void] $listBox.Items.Add('https://localhost:30000')
    [void] $listBox.Items.Add('https://uk1api.wdgautomation.com')
    [void] $listBox.Items.Add('https://eu1api.wdgautomation.com')
    [void] $listBox.Items.Add('https://us1api.wdgautomation.com')
    [void] $listBox.Items.Add('https://ap1api.wdgautomation.com')
    [void] $listBox.Items.Add('https://br2-api.wdgautomation.com')

    $listBox.Height = 70
    $form.Controls.Add($listBox)
    $form.Topmost = $true

    do
    {
        $result = $form.ShowDialog()

        if ($ListBox.SelectedIndices.Count -lt 1 -and $result -eq [System.Windows.Forms.DialogResult]::OK)
        {
           $msgBoxInput =  [System.Windows.MessageBox]::Show('Please select a host','No Host Selected','OK','Error')
        }
    }
    until (($result -eq [System.Windows.Forms.DialogResult]::OK -and $listBox.SelectedIndices.Count -ge 1) -or $result -ne [System.Windows.Forms.DialogResult]::OK)

    $x = $listBox.SelectedItem
    return $x
}

# ######################################################
# Get the RPA username
# ######################################################
function getUsername {

    Add-Type -AssemblyName System.Windows.Forms
    Add-Type -AssemblyName System.Drawing

    $form = New-Object System.Windows.Forms.Form
    $form.Text = 'Enter Tenant Username'
    $form.Size = New-Object System.Drawing.Size(300,200)
    $form.StartPosition = 'CenterScreen'

    $okButton = New-Object System.Windows.Forms.Button
    $okButton.Location = New-Object System.Drawing.Point(75,120)
    $okButton.Size = New-Object System.Drawing.Size(75,23)
    $okButton.Text = 'OK'
    $okButton.DialogResult = [System.Windows.Forms.DialogResult]::OK
    $form.AcceptButton = $okButton
    $form.Controls.Add($okButton)

    $cancelButton = New-Object System.Windows.Forms.Button
    $cancelButton.Location = New-Object System.Drawing.Point(150,120)
    $cancelButton.Size = New-Object System.Drawing.Size(75,23)
    $cancelButton.Text = 'Cancel'
    $cancelButton.DialogResult = [System.Windows.Forms.DialogResult]::Cancel
    $form.CancelButton = $cancelButton
    $form.Controls.Add($cancelButton)

    $promptText = New-Object System.Windows.Forms.Label
    $promptText.Location = New-Object System.Drawing.Point(10,20)
    $promptText.Size = New-Object System.Drawing.Size(280,20)
    $promptText.Text = 'Username:'
    $form.Controls.Add($promptText)

    $username = New-Object System.Windows.Forms.TextBox
    $username.Location = New-Object System.Drawing.Point(10,40)
    $username.Size = New-Object System.Drawing.Size(260,20)
    $username.Text = $defaultUserGbl
    $form.Controls.Add($username)

    $form.Topmost = $true

    $form.Add_Shown({$username.Select()})
    $result = $form.ShowDialog()

    if ($result -eq [System.Windows.Forms.DialogResult]::OK)
    {
        $x = $username.Text
        $x
    } 
}

# ######################################################
# Get the RPA password 
# ######################################################
function getPassword {

    Add-Type -AssemblyName System.Windows.Forms
    Add-Type -AssemblyName System.Drawing

    $form = New-Object System.Windows.Forms.Form
    $form.Text = 'Enter Tenant Password'
    $form.Size = New-Object System.Drawing.Size(300,200)
    $form.StartPosition = 'CenterScreen'

    $okButton = New-Object System.Windows.Forms.Button
    $okButton.Location = New-Object System.Drawing.Point(75,120)
    $okButton.Size = New-Object System.Drawing.Size(75,23)
    $okButton.Text = 'OK'
    $okButton.DialogResult = [System.Windows.Forms.DialogResult]::OK
    $form.AcceptButton = $okButton
    $form.Controls.Add($okButton)

    $cancelButton = New-Object System.Windows.Forms.Button
    $cancelButton.Location = New-Object System.Drawing.Point(150,120)
    $cancelButton.Size = New-Object System.Drawing.Size(75,23)
    $cancelButton.Text = 'Cancel'
    $cancelButton.DialogResult = [System.Windows.Forms.DialogResult]::Cancel
    $form.CancelButton = $cancelButton
    $form.Controls.Add($cancelButton)

    $promptText = New-Object System.Windows.Forms.Label
    $promptText.Location = New-Object System.Drawing.Point(10,20)
    $promptText.Size = New-Object System.Drawing.Size(280,20)
    $promptText.Text = 'Password:'
    $form.Controls.Add($promptText)

    $password = New-Object System.Windows.Forms.TextBox
    $password.Location = New-Object System.Drawing.Point(10,40)
    $password.Size = New-Object System.Drawing.Size(260,20)
    $password.PasswordChar = '*'
    $password.Text = $defaultPasswordGbl
    $form.Controls.Add($password)

    $form.Topmost = $true

    $form.Add_Shown({$password.Select()})
    $result = $form.ShowDialog()

    if ($result -eq [System.Windows.Forms.DialogResult]::OK)
    {
        $x = $password.Text
        $x
    }
}

# ##############################################################
# Get the RPA tenants associated to the given host and username. 
# ##############################################################
function GetTenants {
    param (
        $hostURL,
        $username
    )

    $url = $hostURL + '/v1.0/en-US/account/tenant?username=' + $username

    try {
        $tenantResponse = Invoke-RestMethod $url -Method 'GET' -Headers $headers
    } catch {
        # Dig into the exception to get the Response details.
        $msgBoxInput =  [System.Windows.MessageBox]::Show('Invalid username: ' + $_.Exception.Response.StatusDescription,'Get Tenants','OK','Error')
        Break finish
    }

    $tenantResponse | ConvertTo-Json 

    return $tenantResponse
}

# ######################################################
# Select the tenant
# ######################################################
function SelectTenant {
    param (
        [string]$tenants
    )
    $form = New-Object System.Windows.Forms.Form
    $form.Text = 'Tenant Selection'
    $form.Size = New-Object System.Drawing.Size(300,200)
    $form.StartPosition = 'CenterScreen'

    $OKButton = New-Object System.Windows.Forms.Button
    $OKButton.Location = New-Object System.Drawing.Point(75,120)
    $OKButton.Size = New-Object System.Drawing.Size(75,23)
    $OKButton.Text = 'OK'
    $OKButton.DialogResult = [System.Windows.Forms.DialogResult]::OK
    $form.AcceptButton = $OKButton
    $form.Controls.Add($OKButton)

    $CancelButton = New-Object System.Windows.Forms.Button
    $CancelButton.Location = New-Object System.Drawing.Point(150,120)
    $CancelButton.Size = New-Object System.Drawing.Size(75,23)
    $CancelButton.Text = 'Cancel'
    $CancelButton.DialogResult = [System.Windows.Forms.DialogResult]::Cancel
    $form.CancelButton = $CancelButton
    $form.Controls.Add($CancelButton)

    $label = New-Object System.Windows.Forms.Label
    $label.Location = New-Object System.Drawing.Point(10,20)
    $label.Size = New-Object System.Drawing.Size(280,20)
    $label.Text = 'Please make a selection from the list below:'
    $form.Controls.Add($label)

    $listBox = New-Object System.Windows.Forms.Listbox
    $listBox.Location = New-Object System.Drawing.Point(10,40)
    $listBox.Size = New-Object System.Drawing.Size(260,20)

    $listBox.SelectionMode = 'MultiExtended'

    $data = ConvertFrom-Json $tenants
    $hash = @{}

    foreach ($tenant in $data)
    {
        $hash[$tenant.name] = $tenant.id
    }

    $hash.GetEnumerator() | ForEach-Object {
       "The value of '$($_.Key)' is: $($_.Value)"
       [void] $listBox.Items.Add($_.Key)
    }

    $listBox.Height = 70
    $form.Controls.Add($listBox)
    $form.Topmost = $true

    do
    {
        $result = $form.ShowDialog()

        if ($ListBox.SelectedIndices.Count -lt 1 -and $result -eq [System.Windows.Forms.DialogResult]::OK)
        {
           $msgBoxInput =  [System.Windows.MessageBox]::Show('Please select a tenant','No Tenant Selected','OK','Error')
        }
    }
    until (($result -eq [System.Windows.Forms.DialogResult]::OK -and $listBox.SelectedIndices.Count -ge 1) -or $result -ne [System.Windows.Forms.DialogResult]::OK)

    $x = $listBox.SelectedItem
    $Global:tenantIdGbl = $hash.$x
    return $hash.$x
    
 }

# ########################################################################
# Get the RPA acccess token given the host, username, password and tenant 
# ########################################################################
function getAccessToken {
    param (
        [string]$hostURL,
        [string]$tenantId,
        [string]$username,
        [string]$password
    )
    $url = $hostURL + '/v1.0/token'

    $headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
    $headers.Add("tenantId",$tenantIdGbl)
    $headers.Add("Content-Type", "application/x-www-form-urlencoded")
    $body = "grant_type=password&username=" + $username + "&password=" + $password + "&culture=en-US"

    try {
       $loginResponse = Invoke-RestMethod $url -Method 'POST' -Headers $headers -Body $body
    } catch {
        # Dig into the exception to get the Response details.
        # Note that value__ is not a typo.
        Write-Host "StatusCode:" $_.Exception.Response.StatusCode.value__ 
        Write-Host "StatusDescription:" $_.Exception.Response.StatusDescription
        $msgBoxInput =  [System.Windows.MessageBox]::Show('Login failed: ' + $_.Exception.Response.StatusDescription,'Access token','OK','Error')
        Break finish
    }

    $loginResponse | ConvertTo-Json

    $loginResponse

    $global:accessTokenGbl = $loginResponse.access_token
    
    return $accessTokenGbl    
}

# ######################################################
# Get the RPA processes associated to the tenant. 
# ######################################################
function getProcesses {
    param (
        [string]$hostURL,
        [string]$tenantId,
        [string]$accessToken
    )

    $url = $hostURL + '/v2.0/workspace/' + $tenantIdGbl + '/process?lang=en-US'

    $headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
    $headers.Add("Authorization", "Bearer " + $accessTokenGbl)

    try {
        $response = Invoke-RestMethod $url -Method 'GET' -Headers $headers 
    } catch {
        # Dig into the exception to get the Response details.
        # Note that value__ is not a typo.
        Write-Host "StatusCode:" $_.Exception.Response.StatusCode.value__ 
        Write-Host "StatusDescription:" $_.Exception.Response.StatusDescription
        $msgBoxInput =  [System.Windows.MessageBox]::Show('Get processes failed: ' + $_.Exception.Response.StatusDescription,'Get processes failed','OK','Error')
        Break finish
    }

    $response.results

    return $response.results
}

# ######################################################
# Select a process to run
# ######################################################
function selectProcess {
    param (
        $processes
    )
    $form = New-Object System.Windows.Forms.Form
    $form.Text = 'Process Selection'
    $form.Size = New-Object System.Drawing.Size(300,200)
    $form.StartPosition = 'CenterScreen'

    $OKButton = New-Object System.Windows.Forms.Button
    $OKButton.Location = New-Object System.Drawing.Point(75,120)
    $OKButton.Size = New-Object System.Drawing.Size(75,23)
    $OKButton.Text = 'OK'
    $OKButton.DialogResult = [System.Windows.Forms.DialogResult]::OK
    $form.AcceptButton = $OKButton
    $form.Controls.Add($OKButton)

    $CancelButton = New-Object System.Windows.Forms.Button
    $CancelButton.Location = New-Object System.Drawing.Point(150,120)
    $CancelButton.Size = New-Object System.Drawing.Size(75,23)
    $CancelButton.Text = 'Cancel'
    $CancelButton.DialogResult = [System.Windows.Forms.DialogResult]::Cancel
    $form.CancelButton = $CancelButton
    $form.Controls.Add($CancelButton)

    $label = New-Object System.Windows.Forms.Label
    $label.Location = New-Object System.Drawing.Point(10,20)
    $label.Size = New-Object System.Drawing.Size(280,20)
    $label.Text = 'Please make a selection from the list below:'
    $form.Controls.Add($label)

    $listBox = New-Object System.Windows.Forms.Listbox
    $listBox.Location = New-Object System.Drawing.Point(10,40)
    $listBox.Size = New-Object System.Drawing.Size(260,20)

    $listBox.SelectionMode = 'MultiExtended'

    $hash = @{}

    foreach ($process in $processes)
    {
        $process
        $hash[$process.name] = $process.id
    }

    $hash.GetEnumerator() | ForEach-Object {
       "The value of '$($_.Key)' is: $($_.Value)"
       [void] $listBox.Items.Add($_.Key)
    }

    $listBox.Height = 70
    $form.Controls.Add($listBox)
    $form.Topmost = $true

    do
    {
        $result = $form.ShowDialog()

        if ($ListBox.SelectedIndices.Count -lt 1 -and $result -eq [System.Windows.Forms.DialogResult]::OK)
        {
           $msgBoxInput =  [System.Windows.MessageBox]::Show('Please select a process','No Process Selected','OK','Error')
        }
    }
    until (($result -eq [System.Windows.Forms.DialogResult]::OK -and $listBox.SelectedIndices.Count -ge 1) -or $result -ne [System.Windows.Forms.DialogResult]::OK)
    
    $x = $listBox.SelectedItem
    $Global:processIdGbl = $hash.$x
    return $hash.$x
   
}

# ######################################################
# Get the bot input parameters 
# ######################################################
function getBotDetails {
    param ( 
        [string]$hostURL,
        [string]$tenantId,
        [string]$accessToken,
        [string]$processId
    )

    $headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
    $headers.Add("Authorization", "Bearer " + $accessTokenGbl)

    $url = $hostURL + '/v2.0/workspace/' + $tenantIdGbl + '/process/' + $processIdGbl

    try {
       $response = Invoke-RestMethod $url -Method 'GET' -Headers $headers 
    } catch {
        $response.scriptSchema.inputSchema.properties
        # Dig into the exception to get the Response details.
        # Note that value__ is not a typo.
        Write-Host "StatusCode:" $_.Exception.Response.StatusCode.value__ 
        Write-Host "StatusDescription:" $_.Exception.Response.StatusDescription
        $msgBoxInput =  [System.Windows.MessageBox]::Show('Get bot details failed: ' + $_.Exception.Response.StatusDescription,'Get Bot Details','OK','Error')
        Break finish
    }

    $inputSchema = $response.scriptSchema.inputSchema.properties

    return $inputSchema | ConvertTo-Json
}

# ##############################################################
# Attempt to generate the bot input parameters (work in progress)
# ##############################################################
function getPayload {
    param (
        [String]$inputParams
    )
    
    # transform the inputSchema to the payload
    $inputParams = $inputParams -replace '{', ''
    $inputParams = $inputParams -replace '}', ''
    $inputParams = $inputParams -replace '"type":', ''

    $samplePayload = "{ 
     `"payload`": { 
                 $inputParams  
    } 
 }"

    Add-Type -AssemblyName System.Windows.Forms
    Add-Type -AssemblyName System.Drawing

    $form = New-Object System.Windows.Forms.Form
    $form.Text = 'Payload'
    $form.Size = New-Object System.Drawing.Size(300,400)
    $form.StartPosition = 'CenterScreen'

    $okButton = New-Object System.Windows.Forms.Button
    $okButton.Location = New-Object System.Drawing.Point(75,300)
    $okButton.Size = New-Object System.Drawing.Size(75,23)
    $okButton.Text = 'OK'
    $okButton.DialogResult = [System.Windows.Forms.DialogResult]::OK
    $form.AcceptButton = $okButton
    $form.Controls.Add($okButton)

    $cancelButton = New-Object System.Windows.Forms.Button
    $cancelButton.Location = New-Object System.Drawing.Point(150,300)
    $cancelButton.Size = New-Object System.Drawing.Size(75,23)
    $cancelButton.Text = 'Cancel'
    $cancelButton.DialogResult = [System.Windows.Forms.DialogResult]::Cancel
    $form.CancelButton = $cancelButton
    $form.Controls.Add($cancelButton)

    $promptText = New-Object System.Windows.Forms.Label
    $promptText.Location = New-Object System.Drawing.Point(10,20)
    $promptText.Size = New-Object System.Drawing.Size(280,20)
    $promptText.Text = 'Payload:'
    $form.Controls.Add($promptText)

    $payload = New-Object System.Windows.Forms.TextBox
    $payload.Multiline = $true
    $payload.AcceptsReturn = $true 
    $payload.ScrollBars = "Vertical"
    $payload.Location = New-Object System.Drawing.Point(10,40)
    $payload.Size = New-Object System.Drawing.Size(260,200)
    $payload.Text = $samplePayload

    $form.Controls.Add($payload)

    $form.Topmost = $true

    $form.Add_Shown({$payload.Select()})
    $result = $form.ShowDialog()

    if ($result -eq [System.Windows.Forms.DialogResult]::OK)
    {
        $x = $payload.Text
        $x
    }
}

# ######################################################
# Run the process  
# ######################################################
function runBot {
    param ( 
        [string]$hostURL,
        [string]$tenantId,
        [string]$accessToken,
        [string]$processId,
        [string]$payload
    )
   
    $headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
    $headers.Add("Authorization", "Bearer " + $accessTokenGbl)
    $headers.Add("Content-Type", "application/json")

    $url = $hostURL + '/v2.0/workspace/' + $tenantIdGbl + '/process/' + $processIdGbl + '/instance?lang=en-US'

    $body = $payload

    try {
        $response = Invoke-RestMethod $url -Method 'POST' -Headers $headers -Body $body
    } catch {
        # Dig into the exception to get the Response details.
        # Note that value__ is not a typo.
        Write-Host "StatusCode:" $_.Exception.Response.StatusCode.value__ 
        Write-Host "StatusDescription:" $_.Exception.Response.StatusDescription
        $msgBoxInput =  [System.Windows.MessageBox]::Show('Invocation failed: ' + $_.Exception.Response.StatusDescription,'Invocation failed','OK','Error')
        Break finish
    }

    $response | ConvertTo-Json

    $global:instanceIdGbl = $response.id

    $Msg = 'Instance Id: ' + $instanceIdGbl 

    $msgBoxInput =  [System.Windows.MessageBox]::Show($Msg,'Bot Instance','OK','Information')
}

# ######################################################
# Get the execution resuylt of the process.   
# ######################################################
function getBotResult {
    param ( 
        [string]$hostURL,
        [string]$tenantId,
        [string]$accessToken,
        [string]$processId,
        [string]$instanceId
    )
   
    $headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
    $headers.Add("Authorization", "Bearer " + $accessTokenGbl)

    $url = $hostURL + '/v2.0/workspace/' + $tenantIdGbl + '/process/' + $processIdGbl + '/instance/' + $instanceIdGbl

    $status = 'Started'
    while($status -ne 'done') {

        try {
            $response = Invoke-RestMethod $url -Method 'GET' -Headers $headers
        } catch {
            # Dig into the exception to get the Response details.
            # Note that value__ is not a typo.
            Write-Host "StatusCode:" $_.Exception.Response.StatusCode.value__ 
            Write-Host "StatusDescription:" $_.Exception.Response.StatusDescription
            $msgBoxInput =  [System.Windows.MessageBox]::Show('Invocation failed: ' + $_.Exception.Response.StatusDescription,'Invocation failed','OK','Error')
            Break finish
        }

        $response | ConvertTo-Json

        $status = $response.status

        if ($status -ne 'done') { 

            $Msg = 'Status: ' + $status + '
             Continue?'

            $msgBoxInput =  [System.Windows.MessageBox]::Show($Msg,'Bot Status','YesNo','Information')
            switch  ($msgBoxInput) {
              'No' {
              ## Finish
              Break Script
              }
            }
        }
        else {
                $output = ConvertTo-Json $response.outputs

                $Msg = 'Status: DONE ' + 
                'output: ' + $output + ''

                $msgBoxInput =  [System.Windows.MessageBox]::Show($Msg,'Bot Completed','OK','Information')
            } 
     }
 }


# Disable SSL for test environments.  
# NOTE: For production this must not be used.
DisableSSL

$hostURL = GetHost
$hostURL

$username = getUserName
$username

$password = getPassword
$password

$tenantResponse = getTenants $hostURL $username
$tenantResponse

$tenantId = SelectTenant $tenantResponse
$tenantId

$accessToken = getAccessToken $hostURL $tenantId $username $password
$accessToken

$processes = getProcesses $hostURL $tenantId $accessToken
$processes

$processId = selectProcess $processes

$inputParams = getBotDetails $hostURL $tenantId $accessToken $processId
$inputParams

$payload = getPayload $inputParams
$payload

$instanceId = runBot $hostURL $tenantId $accessToken $processId $payload

getBotResult $hostURL $tenantId $accessToken $processId $instanceId
