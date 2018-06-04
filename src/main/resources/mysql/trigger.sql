drop trigger if exists checgrade//
drop trigger if exists checkgrade//
create trigger checkgrade before insert on studentgrade
for each row
  begin
    if new.value < 2 OR new.value > 5 then
      signal sqlstate '45000' set message_text = 'Grade out of range. Range is <2,5>';
    end if;
  end//

drop trigger if exists checkage//
create trigger checkage before insert on example_users
for each row
  begin
    if new.age < 18 then
      signal sqlstate '45000' set message_text = 'Age is below 18!';
    end if;
  end//
