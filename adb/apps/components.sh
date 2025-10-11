for f in *; do echo $f; grep -A 1 android.intent.action.MAIN: $f; done
