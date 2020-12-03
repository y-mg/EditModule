# EditModule
<br/>
<br/>



## 1. ClearEditView

### XML

| Option | Default | Description(KO) | Description(EN) |
|:----------|:----------|:----------|:----------|
| ceClearButtonIcon | icon_clear.png | 삭제 버튼 아이콘 | Delete icon |

It is recommended to apply options "inputType" and "imeOptions".
<br/>
<br/>



## 2. PasswordEditView

### XML

| Option | Default | Description(KO) | Description(EN) |
|:----------|:----------|:----------|:----------|
| peClearButtonEnabled | true | 삭제 버튼 사용 여부 | Delete button enabled |
| peClearButtonIcon | icon_clear.png | 삭제 버튼 아이콘 | Delete icon |
| pePasswordShowButtonIcon | btn_password_show.png | 비밀번호 보이기 아이콘 | Password show icon |
| pePasswordHideButtonIcon | btn_password_hide.png | 비밀번호 숨기기 아이콘 | Password hide icon |

It is recommended to apply options "imeOptions" and "maxLength".
<br/>
<br/>



## 3. DateOfYearEditView

### XML

| Option | Default | Description(KO) | Description(EN) |
|:----------|:----------|:----------|:----------|
| doyClearButtonEnabled | true | 삭제 버튼 사용 여부 | Delete button enabled |
| doyClearButtonIcon | icon_clear.png | 삭제 버튼 아이콘 | Delete icon |

It is recommended to apply option "imeOptions".
<br/>
<br/>



## 4. NumberFormatEditView

### XML

| Option | Default | Description(KO) | Description(EN) |
|:----------|:----------|:----------|:----------|
| nfClearButtonEnabled | true | 삭제 버튼 사용 여부 | Delete button enabled |
| nfClearButtonIcon | icon_clear.png | 삭제 버튼 아이콘 | Delete icon |
| nfAddEditStart | "" | 앞에 추가할 문자열 | String to add initially |

It is recommended to apply option "imeOptions".

| Function | Parameter | Description(KO) | Description(EN) |
|:----------|:----------|:----------|:----------|
| setAddEditStart(addEditStart: String) | Start String | String to add initially |
| getFormatText() | Void | 숫자 값 가져오기 | Get only number |
<br/>
<br/>



## 5. DecimalFormatEditView

### XML

| Option | Default | Description(KO) | Description(EN) |
|:----------|:----------|:----------|:----------|
| dfClearButtonEnabled | true | 삭제 버튼 허용 여부 | Delete button enabled |
| dfClearButtonIcon | icon_clear.png | 삭제 버튼 아이콘 | Delete icon |
| dfNumberCut | 30 | 정수 자릿수 | Integer digits |
| dfDecimalCut | 8 | 소수 자릿수 | Decimal digits |
| dfAddEditStart | "" | 앞에 추가할 문자열 | String to add initially |

It is recommended to apply option "imeOptions".

### Kotlin

| Option | Default | Description(KO) | Description(EN) |
|:----------|:----------|:----------|:----------|
| setInputFilterDecimalEditView(<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;numberCut: Int,<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;decimalCut: Int<br/>) | Integer Digits,<br/>Decimal Digits | 필터 설정 | Filter setting |
| setAddEditStart(addEditStart: String) | Start String | 앞에 추가할 문자열 | String to add initially |
| getFormatText() | Void | 소수 값 가져오기 | Get only decimal |
<br/>
<br/>


