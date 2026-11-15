# MoneyPilot: два этапа
SE-2509. Manyakina Yuliya и Bazarbayev Bakdaulet.

## Этап 1: часть Бакдаулета
Strategy, Command, Facade. Расчёты, сохранение, команды и история Undo. Лимит истории 100 действий.

## Этап 2: часть Юли
Builder, Observer, Adapter. Builder добавляет именованные шаги построения Transaction. EventBus уведомляет BudgetAlert и DashboardPanel после сохранения. LegacyBankAdapter преобразует CSV в операции, ошибки содержат номер строки. Графическое окно использует все шесть паттернов.

## Проверка на Windows
JDK 17+: test.bat, затем build.bat и run.bat. Скрипт тестов запускает четыре набора проверок. Для демо выбрать 2026-11, Load demo один раз. Income 180000, expenses 80000, net 100000 KZT. CSV import samples/bank-demo.csv добавляет расходы 2500 и доходы 15000, одна отмена убирает весь импорт.
