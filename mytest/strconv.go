/*************************************************************************
	> File Name: strconv.go
	> Author: DavidWang
	> Mail: davidwang2006@qq.com 
	> Created Time: Mon 22 Oct 2012 12:41:20 AM CST
 ************************************************************************/

package main
import (
	"fmt"
	"strconv"
)
func main(){
	i := 1111
	s := strconv.Itoa(i)
	fmt.Println(s)
	var a interface{}= "sdfsdf"
	var v,p = a.(string)
	fmt.Println(v,p)
}
