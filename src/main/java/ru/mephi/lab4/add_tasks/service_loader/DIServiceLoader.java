package ru.mephi.lab4.add_tasks.service_loader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class DIServiceLoader<T> {

    private final Class<T> serviceClass;
    private final Map<String, Class<? extends T>> beanClasses = new LinkedHashMap<>();
    private final Map<String, T> beans = new HashMap<>();

    public DIServiceLoader(Class<T> serviceClass) throws Exception {
        this.serviceClass = serviceClass;
        loadDescriptors();
    }

    private void loadDescriptors() throws Exception {
        String resourceName = "META-INF/services/" + serviceClass.getName();
        Enumeration<URL> urls = Thread.currentThread()
                .getContextClassLoader()
                .getResources(resourceName);

        Set<String> seenNames = new HashSet<>();

        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;

                    // Формат: имя_класса#имя_бина
                    String[] parts = line.split("#");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("Неверный формат: " + line);
                    }

                    String className = parts[0].trim();
                    String beanName  = parts[1].trim();

                    if (!seenNames.add(beanName)) {
                        throw new IllegalStateException(
                                "Дублирующийся бин: " + beanName);
                    }

                    @SuppressWarnings("unchecked")
                    Class<? extends T> cl = (Class<? extends T>)
                            Class.forName(className);
                    beanClasses.put(beanName, cl);
                }
            }
        }
    }

    public T getBean(String beanName) throws Exception {
        if (!beanClasses.containsKey(beanName)) {
            throw new NoSuchElementException("Бин не найден: " + beanName);
        }
        if (!beans.containsKey(beanName)) {
            beans.put(beanName, beanClasses.get(beanName)
                    .getDeclaredConstructor().newInstance());
        }
        return beans.get(beanName);
    }

    public T getBean(Class<?> beanClass) throws Exception {
        ServiceLoader<T> loader = ServiceLoader.load(serviceClass);
        for (T service : loader) {
            if (service.getClass().equals(beanClass)) {
                return service;
            }
        }
        throw new NoSuchElementException("Бин типа " + beanClass.getName() + " не найден");
    }

    public Iterator<T> iterator() {
        return ServiceLoader.load(serviceClass).iterator();
    }
}