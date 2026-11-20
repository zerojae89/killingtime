param(
    [string]$OutputDirectory = (Join-Path $PSScriptRoot 'dist')
)

$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem
$webRoot = Join-Path $PSScriptRoot 'web'
$destination = Join-Path (New-Item -ItemType Directory -Path $OutputDirectory -Force).FullName 'greeting.war'
$tempArchive = Join-Path (Split-Path -Parent $destination) ("greeting-{0}.tmp" -f [guid]::NewGuid())
try {
    [IO.Compression.ZipFile]::CreateFromDirectory($webRoot, $tempArchive)
    Move-Item -LiteralPath $tempArchive -Destination $destination -Force
    Write-Output $destination
} finally {
    if (Test-Path -LiteralPath $tempArchive) {
        Remove-Item -LiteralPath $tempArchive
    }
}
