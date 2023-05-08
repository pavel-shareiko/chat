package by.shareiko.chat.mapper;

import by.shareiko.chat.domain.Message;
import by.shareiko.chat.dto.NewMessageDTO;
import by.shareiko.chat.dto.SimpleMessageDTO;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = StringTrimmer.class)
public interface MessageMapper {
    @Mapping(source = "chatId", target = "chat.id")
    Message newMessageDTOToMessage(NewMessageDTO newMessageDTO);

    NewMessageDTO messageToNewMessageDTO(Message message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Message updateMessageFromNewMessageDTO(NewMessageDTO newMessageDTO, @MappingTarget Message message);

    Message simpleMessageDTOToMessage(SimpleMessageDTO simpleMessageDTO);

    @Mapping(source = "chat.id", target = "chatId")
    SimpleMessageDTO messageToSimpleMessageDTO(Message message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Message updateMessageFromSimpleMessageDTO(SimpleMessageDTO simpleMessageDTO, @MappingTarget Message message);
}
