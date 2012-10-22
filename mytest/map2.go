/*************************************************************************
	> File Name: map2.go
	> Author: DavidWang
	> Mail: davidwang2006@qq.com 
	> Created Time: Mon 22 Oct 2012 12:09:17 AM CST
 ************************************************************************/

package main
import (
	"fmt"
)
func main(){
	map1 := map[string]int{"1":111,"2":222222222}
	for k,v := range map1{
		fmt.Printf("key is %s, value is %d\n",k,v)
	}
	map2 := map[int]string{23:"hello",34:"sdfsd"}
	fmt.Println(map2)
	delete(map2,33)
	v,ok := map2[23]
	fmt.Println(v,ok)

}
