# langchainrag


git commit -m "first commit"

Function GetValueFromUrl(url As String, key As String) As String
Dim queryString As String
Dim params() As String
Dim param As Variant
Dim paramParts() As String

    ' Find the start of the query string
    Dim startPos As Long
    startPos = InStr(url, "?")
    If startPos = 0 Then
        GetValueFromUrl = "" ' No query string found
        Exit Function
    End If

    ' Get the query string part of the URL
    queryString = Mid(url, startPos + 1)

    ' Split the query string into individual parameters
    params = Split(queryString, "&")

    ' Loop through each parameter and find the key
    For Each param In params
        paramParts = Split(param, "=")
        If UBound(paramParts) = 1 Then
            If paramParts(0) = key Then
                GetValueFromUrl = paramParts(1)
                Exit Function
            End If
        End If
    Next param

    ' If key is not found, return an empty string
    GetValueFromUrl = ""
End Function