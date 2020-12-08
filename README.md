# EditModule
<img width="250px" height="500px" src="/sample/clear.gif" /> <img width="250px" height="500px" src="/sample/password.gif" /> <img width="250px" height="500px" src="/sample/date.gif" />
<img width="250px" height="500px" src="/sample/number.gif" /> <img width="250px" height="500px" src="/sample/decimal.gif" />
<br/>
<br/>



## 1. ClearEditView

> 이것은 클리어 버튼을 설정할 수 있는 EditText 입니다.<br/>
> This is EditText where you can set the Clear button.


### XML Attributes

```kotlin
<com.ymg.editmodule.clear.ClearEditView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:inputType="text"
    android:imeOptions="actionDone"
    app:ceClearButtonIcon="@drawable/icon_clear" />
```
- app:ceClearButtonIcon
    - 클리어의 버튼 아이콘을 설정한다.
    - Set the icon for the clear button.

- inputType and imeOptions
    - 이 옵션을 적용하는 것을 추천합니다.
    - It is recommended to apply options.
<br/>
<br/>



## 2. PasswordEditView

> 이것은 비밀번호 보안성을 설정할 수 있는 EditText 입니다.<br/>
> This is EditText, which allows you to set password security.


### XML Attributes

```kotlin
<com.ymg.editmodule.password.PasswordEditView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:imeOptions="actionDone"
    android:maxLength="20"
    app:peClearButtonEnabled=true
    app:peClearButtonIcon="@drawable/icon_clear"
    app:peShowPasswordButtonIcon="@drawable/icon_show"
    app:peHidePasswordButtonIcon="@drawable/icon_hide" />
```
- app:peClearButtonEnabled
    - 클리어 버튼의 사용 여부를 설정한다.
    - Set whether or not to use the clear button.

- app:peClearButtonIcon
    - 클리어 버튼의 아이콘을 설정한다.
    - Set the icon for the clear button.

- app:peShowPasswordButtonIcon
    - 비밀번호 보이기 버튼의 아이콘을 설정한다.
    - Set the icon for the Show Password button.

- app:peHidePasswordButtonIcon
    - 비밀번호 숨기기 버튼의 아이콘을 설정한다.
    - Set the icon for the Hide Password button.

- imeOptions and maxLength
    - 이 옵션을 적용하는 것을 추천합니다.
    - It is recommended to apply options.
<br/>
<br/>



## 3. DateOfYearEditView

> 이것은 생년월일을 설정할 수 있는 EditText 입니다.<br/>
> This is EditText, where you can set the date of birth.


### XML Attributes

```kotlin
<com.ymg.editmodule.dateofyear.DateOfYearEditView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:imeOptions="actionDone"
    app:doyClearButtonEnabled=true
    app:doyClearButtonIcon="@drawable/icon_clear" />
```
- app:doyClearButtonEnabled
    - 클리어 버튼의 사용 여부를 설정한다.
    - Set whether or not to use the clear button.

- app:doyClearButtonIcon
    - 클리어 버튼의 아이콘을 설정한다.
    - Set the icon for the clear button.

- imeOptions
    - 이 옵션을 적용하는 것을 추천합니다.
    - It is recommended to apply options.
<br/>
<br/>



## 4. NumberFormatEditView

> 이것은 정수를 천 단위일 때마다 "," 로 분리하는 EditText 입니다.<br/>
> This is a EditText that separates the integer into "," every thousand units.


### XML Attributes

```kotlin
<com.ymg.editmodule.number.NumberFormatEditView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:imeOptions="actionDone"
    app:nfClearButtonEnabled=true
    app:nfClearButtonIcon="@drawable/icon_clear"
    app:nfAddEditStart="$"/>
```
- app:nfClearButtonEnabled
    - 클리어 버튼의 사용 여부를 설정한다.
    - Set whether or not to use the clear button.

- app:nfClearButtonIcon
    - 클리어 버튼의 아이콘을 설정한다.
    - Set the icon for the clear button.

- app:nfAddEditStart
    - 맨 앞에 문자열을 추가한다.
    - Add a string to the beginning.

- imeOptions
    - 이 옵션을 적용하는 것을 추천합니다.
    - It is recommended to apply options.


### Kotlin Function

```kotlin
/**
 * - 맨 앞에 문자열을 추가한다.
 * - Add a string to the beginning.
 *
 * @param addEditStart -> Value to be added first
 */
fun setAddEditStart(
    addEditStart: String,
)

/**
 * - 오직 정수 값을 가져온다.
 * - Only take a integer value.
 */
fun getFormatText()
```
<br/>
<br/>



## 5. DecimalFormatEditView

> 이것은 소수를 천 단위일 때마다 "," 로 분리하는 EditText 입니다.<br/>
> This is a EditText that separates the decimal number into "," every thousand units.


### XML Attributes

```kotlin
<com.ymg.editmodule.decimal.DecimalFormatEditView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:imeOptions="actionDone"
    app:dfClearButtonEnabled=true
    app:dfClearButtonIcon="@drawable/icon_clear"
    app:dfNumberCut="10"
    app:dfDecimalCut="6"
    app:dfAddEditStart="코인: "/>
```
- app:dfClearButtonEnabled
    - 클리어 버튼의 사용 여부를 설정한다.
    - Set whether or not to use the clear button.

- app:dfClearButtonIcon
    - 클리어 버튼의 아이콘을 설정한다.
    - Set the icon for the clear button.

- app:dfNumberCut
    - 정수의 자릿수이다.
    - It's an integer number.

- app:dfDecimalCut
    - 소수점 이하 자릿수이다.
    - It's a decimal place.

- app:dfAddEditStart
    - 맨 앞에 문자열을 추가한다.
    - Add a string to the beginning.

- imeOptions
    - 이 옵션을 적용하는 것을 추천합니다.
    - It is recommended to apply options.


### Kotlin Function

```kotlin
/**
 * - 입력 필터를 설정한다.
 * - Set the input filter.
 *
 * @param numberCut -> Integer Digit
 *
 * @param decimalCut -> Decimal Digit
 */
fun setInputFilter(
    numberCut: Int,
    decimalCut: Int
)

/**
 * - 맨 앞에 문자열을 추가한다.
 * - Add a string to the beginning.
 *
 * @param addEditStart -> Value to be added first
 */
fun setAddEditStart(
    addEditStart: String,
)

/**
 * - 오직 소수 값을 가져온다.
 * - Only take a decimal value.
 */
fun getFormatText()
```
<br/>
<br/>


