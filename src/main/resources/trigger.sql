drop trigger if exists checgrade//
drop trigger if exists checkgrade//
create trigger checkgrade before insert on studentgrade
for each row
  begin
    if new.value < 2 OR new.value > 5 then
      signal sqlstate '45000' set message_text = 'Grade out of range. Range is <2,5>';
    end if;
  end//
