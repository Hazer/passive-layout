# passive-layout
Android layouts that selectively respect `match_parent`

### Purpose
Use this library when you want a View to fill its parent, but not expand its bounds.

### Usage

```xml
<com.levelmoney.passivelayout.PassiveFrameLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <!-- This view will expand its parent to fit the image -->
    <ImageView
        android:src="@drawable/something"
        android:layout_width="48dp"
        android:layout_height="wrap_content"
        android:adjustViewBounds="true"/>

    <!-- This view will fill its parent, but not expand it -->
    <TextView
        app:passive_vertical="true"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"/>

</com.levelmoney.passivelayout.PassiveFrameLayout>
```

### Install

```gradle
dependencies {
  compile 'com.levelmoney.passive-layout:passive-layout:1.0-SNAPSHOT'
}
```
