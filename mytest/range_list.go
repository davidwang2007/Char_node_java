/*************************************************************************
	> File Name: range_list.go
	> Author: DavidWang
	> Mail: davidwang2006@qq.com 
	> Created Time: Mon 22 Oct 2012 12:19:19 AM CST
 ************************************************************************/

package main
import (
	"fmt"
)
func main(){
	l := []string{"a","b","c"}
	for k,v := range l{
		fmt.Printf("%d is %s\n",k,v)
	}
	fmt.Println(string([]rune(1)))
}
