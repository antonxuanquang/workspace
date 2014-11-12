#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

int main()
{
	char root[1024];
	getcwd(root, 1024);
	int len = strlen(root);
	if(root[len - 1] != '/')
	{
        root[len] = '/';
        root[len + 1] = '\0';
	}
    else
    {
        root[len] = '\0';
    }

	char java_home[1024];
	strcpy(java_home, root);
	strcat(java_home, "/runtime/bin/java\0");

	execl(java_home, "java", "-jar", "./client.jar", root, NULL);
	return 0;
}
