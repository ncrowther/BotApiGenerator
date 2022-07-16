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

    $result = $form.ShowDialog()

    if ($result -eq [System.Windows.Forms.DialogResult]::OK)
    {
        $x = $listBox.SelectedItems
        $x
    }
}

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
    $username.Text = 'ncrowther@uk.ibm.com'
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
    $password.Text = 'Porker01!!'
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

function getPayload {

    $samplePayload = "{
    `n    `"payload`": {
    `n        `"in_region`": `"328328`"
    `n    }
    `n}"

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

function GetTenants {
    param (
        $hostURL,
        $username
    )

    $url = $hostURL + '/v1.0/en-US/account/tenant?username=' + $username

    $tenantResponse = Invoke-RestMethod $url -Method 'GET' -Headers $headers
    $tenantResponse | ConvertTo-Json 

    return $tenantResponse
}

function SelectTenant {
    param (
        [string]$tenants
    )
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

    $result = $form.ShowDialog()

    if ($result -eq [System.Windows.Forms.DialogResult]::OK)
    {
        $x = $listBox.SelectedItem
        $Global:tenantIdGbl = $hash.$x
        return $hash.$x
    }
}

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
    $loginResponse = Invoke-RestMethod $url -Method 'POST' -Headers $headers -Body $body
    $loginResponse | ConvertTo-Json

    $global:accessTokenGbl = $loginResponse.access_token

    $Msg = '
     Access token: ' +
     $accessTokenGbl

    $msgBoxInput =  [System.Windows.MessageBox]::Show($Msg,'Access token','OK','Information')
    
    return $accessTokenGbl    
}

function getProcesses {
    param (
        [string]$hostURL,
        [string]$tenantId,
        [string]$accessToken
    )

    $url = $hostURL + '/v2.0/workspace/' + $tenantIdGbl + '/process?lang=en-US'

    $headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
    $headers.Add("Authorization", "Bearer " + $accessTokenGbl)

    $response = Invoke-RestMethod $url -Method 'GET' -Headers $headers 
    $response | ConvertTo-Json

    #$global:processIdGbl = $response.results[0].id
    #$processName = $response.results[0].name

    #$Msg = 'Process name: ' + $processName + '
     #Process id: ' + $processIdGbl

    #$msgBoxInput =  [System.Windows.MessageBox]::Show($Msg,'Process Info','OK','Information')

    return $response
}

function selectProcess {
    param (
        [string]$processes
    )
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

    $data = ConvertFrom-Json $processes
    $hash = @{}

    foreach ($process in $data)
    {
        $hash[$process.results[0].name] = $process.results[0].id
    }

    $hash.GetEnumerator() | ForEach-Object {
       "The value of '$($_.Key)' is: $($_.Value)"
       [void] $listBox.Items.Add($_.Key)
    }

    $listBox.Height = 70
    $form.Controls.Add($listBox)
    $form.Topmost = $true

    $result = $form.ShowDialog()

    if ($result -eq [System.Windows.Forms.DialogResult]::OK)
    {
        $x = $listBox.SelectedItem
        $Global:processIdGbl = $hash.$x
        return $hash.$x
    }
}

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
    $response = Invoke-RestMethod $url -Method 'GET' -Headers $headers 
    $response | ConvertTo-Json

    $inputSchema = $response.scriptSchema.inputSchema.properties
    $outputSchema = $response.scriptSchema.outputSchema.properties

    $inputParams = $inputSchema | ConvertTo-Json
    $outputParams = $outputSchema | ConvertTo-Json

    $Msg = 'Bot Input: ' + $inputParams + '
      Bot Output: ' + $outputParams

    $msgBoxInput =  [System.Windows.MessageBox]::Show($Msg,'Bot Schema','OK','Information')

    foreach ($inputParam in $inputSchema) {
       $param = $($inputParam | Get-Member -MemberType *Property).Name
       $param       
       $paramType = $inputParam.$param.type
       $paramType
    } 

   
}

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


    $response = Invoke-RestMethod $url -Method 'POST' -Headers $headers -Body $body
    $response | ConvertTo-Json

    $global:instanceIdGbl = $response.id

    $Msg = 'Instance Id: ' + $instanceIdGbl 

    $msgBoxInput =  [System.Windows.MessageBox]::Show($Msg,'Bot Instance','OK','Information')
}

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

        $response = Invoke-RestMethod $url -Method 'GET' -Headers $headers 
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

getBotDetails $hostURL $tenantId $accessToken $processId

$payload = getPayload
$payload

$instanceId = runBot $hostURL $tenantId $accessToken $processId $payload

getBotResult $hostURL $tenantId $accessToken $processId $instanceId