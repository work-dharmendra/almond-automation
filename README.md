**Almond Automation** is codeless web functional testing tool based on selenium grid.

It is free and open source. It provides UI to create testcase and run those testcases on selenium grid.

You can then view status and screenshot of each step of testcase.

Take a quick look below
![Almond Automation Demo](https://3.bp.blogspot.com/-rL6riJfHaeI/WuEvqt32IxI/AAAAAAAAENU/MxB5ep3zEZ4IyDF0VDbNL91q8vqqmZEagCLcBGAs/s1600/automation-speed.gif)

Almond Automation provides following feature
-  UI to create and run testcase.
- You can manage multiple projects from single UI.
- You can run testcase on any environment. 
- Create TestSuite.
- Support for modular testcase. No need to change many testcases because of change in one field etc.
- Verify execution of testcase easily in UI. View screenshot of each step of testcase.
- It is created keeping maintenance of testcase at most priority.
- QA and Dev both can create testcase easily. Dev can run complete testsuite on his/her machine before pushing build to QA environment.
- Support for macros to run same testcase with different values.

Please visit [**Almond Automation**](http://almond-automation.blogspot.com/p/home.html) for complete documentation.

You can use docker for quick testing.

Create docker-compose.yaml
```yaml
selhub:
  image: selenium/hub
  ports:
    - 4444:4444

nodeff:
  image: selenium/node-firefox-debug
  ports:
    - 5900
  links:
    - selhub:hub

nodechrome:
  image: selenium/node-chrome-debug
  ports:
    - 5900
  links:
    - selhub:hub

almond-automation:
  image: dharmendra82/almond-automation:latest
  ports:
    - 8080:8080
    - 3306:3306
  links:
    - selhub:hub
```

Run below command to start **Almond Automation**

```bash
docker-compose up -d
```

This will create everything(mariadb, selenium grid) and you don't need to install anything.

Access **Almond Automation** at below url

```html
http://localhost:8080/automation/
```
