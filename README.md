# Scala CSV parser
A simple CSV parser inspired by python's csv.

## Usage

```scala
import org.github.takoi.csv
import org.github.takoi.csv._

val rows = csv.read("example.csv", hasHeader=true)

for ((year, make, model, length) <- rows) {
  if (year.toInt <= 2005)
    printf("%s %s\n", make, model)
}
```

**example.csv**
```csv
Year,Make,Model,Length
1997,Ford,E350,2.34
2000,Mercury,Cougar,2.38
```
