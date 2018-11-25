package io.relayr.sensors.init;

class GithubUtils {

    static String prepareUrl(String url) {
        if (isGithubUrl(url)) {
            return changeToGithubRawUrl(url);
        } else {
            return url;
        }
    }

    static boolean isGithubUrl(String url) {
        return url != null && url.matches("^(http|https)://github.com.*$");
    }

    private static String changeToGithubRawUrl(String githubUrl) {
        return githubUrl.replaceAll("/blob", "")
                .replaceAll("//github", "//raw.githubusercontent");
    }

}
