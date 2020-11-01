#### 使用本地文件local file 解决构建Gradle速度慢
1. Gradle 下载的速度真的是很慢。即使科学上网之后还是很慢，可能和我没设置proxy有关系
2. 但是从浏览器下载的速度很快。所以那么直接使用下载的zip local file 文件来构建。
3. 将下载的Gradle 文件放到.gradle 文件夹下。Android Studio 都会在此文件下wrapper/dist来寻找不同版本的构建工具。
   <img src="https://raw.githubusercontent.com/wdfHPY/PersonalPictureBed/pic/imgK_V3%5D)_Z19%5B%60X)Q5%60P%60TUCA.png"/>
4. 之后在Android Studio 在项目的 gradle-wrapper.properties 下设置distrutionUrl 参数
5. 之前的参数应该是Gradle 构建zip 的下载地址。现在改成本地文件即可。
6. `distributionUrl=file:///D:/Gradle/wrapper/dists/gradle-6.1.1-all.zip`
7. 参考：https://stackoverflow.com/questions/22896569/how-to-use-gradle-zip-in-local-system-without-downloading-when-using-gradle-wrap

