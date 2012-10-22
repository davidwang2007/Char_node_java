/*************************************************************************
	> File Name: anoy.go
	> Author: DavidWang
	> Mail: davidwang2006@qq.com 
	> Created Time: Sun 21 Oct 2012 11:35:26 PM CST
 ************************************************************************/

package main
import (
	"fmt"
)
const (
	i3 = 55
)
func main(){
	s := "abc"
	arr := []byte(s)
	fmt.Println("Origin array is ",arr)
	arr[1] = 'd'
	fmt.Println("now array is ",arr)
	fmt.Println("now the string is",string(arr))

	longString := `hello there
who are you baby
? really `
	fmt.Println("The long string is \n",longString)
	fmt.Println(i3)
}
