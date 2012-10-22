/*************************************************************************
	> File Name: 100char.go
	> Author: DavidWang
	> Mail: davidwang2006@qq.com 
	> Created Time: Mon 22 Oct 2012 07:47:16 PM CST
 ************************************************************************/

package main
import (
	"fmt"
	"reflect"
)
func main(){
	fmt.Println("hello I will print 100 chars");
	c := "A"
	fmt.Println(reflect.TypeOf(c))
	sum := 0;
	for i:=1;;i++{
		flag := false;
		for j:=1;j<=i;j++{
			fmt.Printf("%s",c)
			sum++
			if sum == 100 {
				flag = true
				break
			}
		}
		fmt.Println()
		if flag {
			break
		}
	}
}
